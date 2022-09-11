package me.hapyl.scavenger.task;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.hapyl.scavenger.Team;
import me.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TaskCompletion {

    private final Task<?> task;
    private final LinkedHashMap<Team, Integer> completedTimes;
    private final List<Team> completed;
    // Just a visual to see who completed the task in a team.
    private final Map<Task<?>, Player> playersCompleted;

    public TaskCompletion(Task<?> task) {
        this.task = task;
        this.completedTimes = Maps.newLinkedHashMap();
        this.completed = Lists.newArrayList();
        this.playersCompleted = Maps.newHashMap();
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

    public boolean isComplete(Team team) {
        return getCompletion(team) >= task.getAmount();
    }

    public void addCompletion(Team team, Player player) {
        if (isComplete(team)) {
            return;
        }

        // get next reward before adding a team
        final int next = getCompletion(team) + 1;
        completedTimes.put(team, next);

        team.message("&a%s &7advanced &b%s &7task. &8(%s/%s)", player.getName(), task.getName(), next, task.getAmount());

        if (isComplete(team)) {
            completed.add(team);
            playersCompleted.put(task, player);

            // broadcast
            Chat.broadcast("&6&lSCAVENGER! &a%s &7(%s&7) completed &a%s&7!", player.getName(), team.getName(), task.getName());
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

}
