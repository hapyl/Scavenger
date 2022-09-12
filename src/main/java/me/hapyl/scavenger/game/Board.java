package me.hapyl.scavenger.game;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.TaskCompletion;
import me.hapyl.scavenger.task.Type;
import me.hapyl.scavenger.task.tasks.*;
import me.hapyl.spigotutils.module.util.ThreadRandom;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Board {

    private long startedAt;
    private final long timeLimit;

    private final List<Task<?>> tasks;
    private final Map<Task<?>, TaskCompletion> taskCompleted;
    private final Map<Team, Integer> teamPoints;

    public Board(long timeLimitInMinutes) {
        this.timeLimit = timeLimitInMinutes * 60000;
        this.tasks = Lists.newArrayList();
        this.taskCompleted = Maps.newHashMap();
        this.teamPoints = Maps.newHashMap();
        this.startedAt = 0L;

        generateTasks();
    }

    public int getPoints(Team team) {
        return teamPoints.computeIfAbsent(team, t -> 0);
    }

    public void addPoints(Team team, int points) {
        teamPoints.compute(team, (a, b) -> b == null ? 0 : b + points);
    }

    public boolean isStarted() {
        return startedAt != 0L;
    }

    public void start() {
        startedAt = System.currentTimeMillis();
    }

    public List<Task<?>> getTasks() {
        return tasks;
    }

    public <T> Task<?> findTask(Type<T> type, T t) {
        for (Task<?> task : tasks) {
            if (task.getType() == type && task.getT() == t) {
                return task;
            }
        }

        return null;
    }

    public List<TaskCompletion> getTaskInProgress(Team team) {
        final List<TaskCompletion> list = Lists.newArrayList();
        taskCompleted.forEach((task, completion) -> {
            final int amount = completion.getCompletion(team);
            if (amount > 0 && amount < task.getAmount()) {
                list.add(completion);
            }
        });
        return list;
    }

    public TaskCompletion getTaskCompletion(Task<?> task) {
        return taskCompleted.computeIfAbsent(task, TaskCompletion::new);
    }

    public void generateTasks() {
        for (int i = 0; i < 25; i++) {
            final int random = ThreadRandom.nextInt(5);

            if (false) {
                tasks.add(new GatherItem(randomAllowedElement(Type.GATHER_ITEM), range(4, 8)));
                continue;
            }

            switch (random) {
                case 0 -> tasks.add(new BreedAnimal(randomAllowedElement(Type.BREED_ANIMAL), range(1, 4)));
                case 1 -> tasks.add(new CraftItem(randomAllowedElement(Type.CRAFT_ITEM), range(1, 2)));
                case 2 -> tasks.add(new DieFromCause(randomAllowedElement(Type.DIE_FROM_CAUSE)));
                case 3 -> tasks.add(new GatherItem(randomAllowedElement(Type.GATHER_ITEM), range(2, 6)));
                case 4 -> tasks.add(new SlayEntity(randomAllowedElement(Type.KILL_ENTITY), range(1, 5)));
            }
        }
    }

    private <T> T randomAllowedElement(Type<T> type) {
        final T random = type.randomAllowed();
        for (Task<?> task : tasks) {
            if (task.getT().equals(random)) {
                return randomAllowedElement(type);
            }
        }
        return random;
    }

    private int range(int min, int max) {
        return ThreadRandom.nextInt(min, max);
    }

    public long getStartedAt() {
        return startedAt;
    }

    public long getTimeLimit() {
        return timeLimit;
    }

    public void stop() {

    }

    public int getTasksCompleted(UUID uuid) {
        int times = 0;
        for (TaskCompletion completion : taskCompleted.values()) {
            for (Player player : completion.getPlayersCompleted()) {
                if (player.getUniqueId() == uuid) {
                    times++;
                    break;
                }
            }
        }
        return times;
    }
}
