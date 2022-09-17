package me.hapyl.scavenger.scoreboard;

import com.google.common.collect.Maps;
import me.hapyl.scavenger.Inject;
import me.hapyl.scavenger.Main;
import me.hapyl.scavenger.game.Board;
import me.hapyl.scavenger.game.Manager;
import me.hapyl.scavenger.game.Team;
import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.TaskCompletion;
import me.hapyl.scavenger.utils.StringConcator;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.scoreboard.Scoreboarder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class UIManager extends Inject {

    private final Map<Player, Scoreboarder> playerScore;
    private final Manager manager;

    public UIManager(Main main) {
        super(main);
        playerScore = Maps.newHashMap();
        manager = getPlugin().getManager();
    }

    public void createScoreboard(Player player) {
        playerScore.put(player, new Scoreboarder("&6&lSCAVENGER"));
    }

    public void updateTablist(Player player) {
        player.setPlayerListName(generatePlayerListName(player));
        setHeader(player, "", "&6&lSCAVENGER", "");
        generateFooter(player);
    }

    public void generateFooter(Player player) {
        final Board board = manager.getBoard();
        final Team team = Team.getTeam(player);

        if (board == null) {
            setFooter(player, "", "&eWaiting for game to begin...", "");
        }
        else {
            final StringConcator sc = new StringConcator();
            sc.addEmpty();
            if (team == null) {
                sc.add("&cNot in a team!");
            }
            else {
                // Team display
                sc.add("&f&lYou Team: &7(&e&l%s&7)", board.getPoints(team));
                final Set<UUID> players = team.getUUIDs();
                for (UUID uuid : players) {
                    final OfflinePlayer mate = Bukkit.getOfflinePlayer(uuid);
                    sc.add("%s%s &7(&b%s&7)".formatted(team.getColor(), mate.getName(), board.getTasksCompleted(uuid)));
                }

                // Team in progress tasks
                sc.add("");
                sc.add("&f&lIn Progress Tasks:");
                final List<TaskCompletion> progress = board.getTaskInProgress(team);
                if (progress.isEmpty()) {
                    sc.add("&8None!");
                }
                else {
                    int shown = 0;
                    for (TaskCompletion completion : progress) {
                        if (shown >= 5) {
                            sc.add("&8And %s more!", progress.size() - shown);
                            break;
                        }

                        final Task<?> task = completion.getTask();
                        sc.add("&a%s &7(%s&7/%s&7)".formatted(task.getName(), completion.getCompletion(team), task.getAmount()));
                        shown++;
                    }
                }

                // Pinned tasks
                sc.addEmpty();
                sc.add("&f&lPinned Tasks:");
                final Set<Task<?>> pinned = board.getPinnedTasks(player);
                if (pinned.isEmpty()) {
                    sc.add("&8None!");
                    sc.add("&7Pin tasks by clicking at them.");
                    sc.add("&7Pinned tasks are NOT shared.");
                }
                else {
                    int shown = 0;
                    for (Task<?> task : pinned) {
                        if (shown >= 5) {
                            sc.add("&8And %s more!", pinned.size() - shown);
                            break;
                        }

                        final TaskCompletion completion = board.getTaskCompletion(task);
                        sc.add("&a%s &7(%s&7/%s&7)".formatted(task.getName(), completion.getCompletion(team), task.getAmount()));
                        shown++;
                    }
                }
            }

            setFooter(player, sc.toString());
        }
    }

    public void updateScoreboard(Player player) {
        final Scoreboarder score = playerScore.get(player);
        if (score == null) {
            createScoreboard(player);
            updateScoreboard(player);
            return;
        }

        final Board board = manager.getBoard();

        // Lobby
        if (board == null) {
            score.setLines("", "&eWaiting for game to begin...", "");
        }
        else {
            final List<Team> topTeams = board.getTopTeams(3);

            score.setLines(
                    "&8#" + board.getWorld().getHexName(),
                    "",
                    "&fTime Left: &e" + board.getTimeLeftString(),
                    "",
                    "&fTop Teams: ",
                    " &7- " + (topTeams.size() > 0 ? topTeams.get(0).getNameCaps() : "&8???"),
                    " &7- " + (topTeams.size() > 1 ? topTeams.get(1).getNameCaps() : "&8???"),
                    " &7- " + (topTeams.size() > 2 ? topTeams.get(2).getNameCaps() : "&8???"),
                    "",
                    "&e/scavenger &fto see board"
            );
        }

        score.addPlayer(player);
    }

    public String generatePlayerListName(Player player) {
        final Team team = Team.getTeam(player);
        return Chat.format(
                "%s%s%s",
                player.isOp() ? "&c⭐ " : "",
                team == null ? "&b" : (team.getNameCaps() + team.getColor() + " "),
                player.getName()
        );
    }

    private void format(Player player, boolean h, String... lines) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            final String line = lines[i];
            builder.append(line);
            if (i < lines.length - 1) {
                builder.append("\n");
            }
        }

        if (h) {
            player.setPlayerListHeader(Chat.format(builder.toString()));
        }
        else {
            player.setPlayerListFooter(Chat.format(builder.toString()));
        }
    }

    private void setHeader(Player player, String... lines) {
        format(player, true, lines);
    }

    private void setFooter(Player player, String... lines) {
        format(player, false, lines);
    }

    public void updateAll() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            updateScoreboard(player);
            updateTablist(player);
        });
    }
}
