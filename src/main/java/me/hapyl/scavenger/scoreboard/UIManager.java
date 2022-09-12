package me.hapyl.scavenger.scoreboard;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.hapyl.scavenger.Main;
import me.hapyl.scavenger.game.Board;
import me.hapyl.scavenger.game.Manager;
import me.hapyl.scavenger.game.Team;
import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.TaskCompletion;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.scoreboard.Scoreboarder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class UIManager {

    private final Map<Player, Scoreboarder> playerScore;
    private final Manager manager;

    public UIManager() {
        playerScore = Maps.newHashMap();
        manager = Main.getManager();
    }

    public void createScoreboard(Player player) {
        playerScore.put(player, new Scoreboarder("&e&lSCAVENGER"));
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
            final List<String> list = Lists.newArrayList();
            list.add("");
            if (team == null) {
                list.add("&cNot in a team!");
            }
            else {
                list.add("&eYou're in %s &eteam!".formatted(team.getNameCaps()));
                list.add("");
                list.add("&7Teammates:");
                final Set<UUID> players = team.getPlayers();
                for (UUID uuid : players) {
                    final OfflinePlayer mate = Bukkit.getOfflinePlayer(uuid);
                    list.add("%s%s &7(&b%s&7)".formatted(team.getColor(), mate.getName(), board.getTasksCompleted(uuid)));
                }

                list.add("");
                list.add("&7Team Score: &e&l" + board.getPoints(team));
                list.add("");
                list.add("&7In Progress Tasks:");
                final List<TaskCompletion> progress = board.getTaskInProgress(team);
                if (progress == null) {
                    list.add("&8None!");
                }
                else {
                    int shown = 0;
                    for (TaskCompletion completion : progress) {
                        if (shown >= 5) {
                            list.add("&8And %s more!".formatted(progress.size() - shown));
                            list.add("&8Use &e/scavenger &8to see!");
                            break;
                        }
                        final Task<?> task = completion.getTask();
                        list.add("&a%s &7(%s&7/%s&7)".formatted(task.getName(), completion.getCompletion(team), task.getAmount()));
                        shown++;
                    }
                }
            }

            list.add("");

            setFooter(player, list.toArray(new String[] {}));
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
            score.setLines("", "&eWaiting for game", "&eto begin...", "");
        }
        else {
            score.setLines("game progress", "game progress", "game progress", "game progress");
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
