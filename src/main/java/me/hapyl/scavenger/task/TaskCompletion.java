package me.hapyl.scavenger.task;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.hapyl.scavenger.Main;
import me.hapyl.scavenger.game.Board;
import me.hapyl.scavenger.game.Team;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.player.PlayerLib;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.List;

public class TaskCompletion {

    private final Task<?> task;
    private final LinkedHashMap<Team, Integer> completedTimes;
    private final List<Team> completed;
    // Just a visual to see who completed the task in a team.
    private final List<Player> playersCompleted;

    public TaskCompletion(Task<?> task) {
        this.task = task;
        this.completedTimes = Maps.newLinkedHashMap();
        this.completed = Lists.newArrayList();
        this.playersCompleted = Lists.newArrayList();
    }

    public int getNextPointsAward() {
        return switch (completed.size()) {
            case 0 -> 10;
            case 1 -> 7;
            default -> 5;
        };
    }

    public int getCompletion(Team team) {
        return completedTimes.getOrDefault(team, 0);
    }

    public int getCompletion(Player player) {
        final Team team = Team.getTeam(player);
        if (team == null) {
            return 0;
        }
        return getCompletion(team);
    }

    public boolean isComplete(Team team) {
        return getCompletion(team) >= task.getAmount();
    }

    public boolean isComplete(Player player) {
        final Team team = Team.getTeam(player);
        return team != null && isComplete(team);
    }

    public void addCompletion(Player player, int amount) {
        final Team team = Team.getTeam(player);
        if (team == null) {
            return;
        }

        addCompletion(team, player, amount);
    }

    public void addCompletion(Team team, Player player, int amount) {
        if (isComplete(team)) {
            return;
        }

        // get next reward before adding a team
        final int next = getCompletion(team) + amount;
        completedTimes.put(team, next);

        team.message("&a%s &7advanced &b%s &7task. &8(%s/%s)", player.getName(), task.getName(), next, task.getAmount());
        team.getPlayers().forEach(tm -> {
            PlayerLib.playSound(tm, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2.0f);
        });


        if (isComplete(team)) {
            final Board board = Main.getPlugin().getManager().getBoard();
            if (board == null) {
                Chat.broadcast("&4Completion with no board???");
                return;
            }

            final int nextPointsAward = getNextPointsAward();
            board.addPoints(team, nextPointsAward);
            completed.add(team);
            playersCompleted.add(player);

            // broadcast
            Chat.broadcast(
                    "&6&lSCAVENGER! &a%s &7(%s&7) completed &a%s&7! &6&l+%s points!",
                    player.getName(),
                    team.getName(),
                    task.getName(),
                    nextPointsAward
            );
            team.getPlayers().forEach(tm -> {
                PlayerLib.playSound(tm, Sound.ENTITY_PLAYER_LEVELUP, 1.25f);
            });
        }
    }

    @Nullable
    public Team getFirstTeam() {
        if (completed.size() >= 1) {
            return completed.get(0);
        }
        return null;
    }

    @Nullable
    public Team getSecondTeam() {
        if (completed.size() >= 2) {
            return completed.get(1);
        }
        return null;
    }

    public Task<?> getTask() {
        return task;
    }

    public List<Player> getPlayersCompleted() {
        return playersCompleted;
    }
}
