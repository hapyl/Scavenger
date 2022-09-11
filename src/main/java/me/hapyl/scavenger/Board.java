package me.hapyl.scavenger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.TaskCompletion;
import me.hapyl.scavenger.task.Type;
import me.hapyl.scavenger.task.tasks.*;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.util.ThreadRandom;

import java.util.List;
import java.util.Map;

public class Board {

    private long startedAt;
    private final long timeLimit;

    private final List<Task<?>> tasks;
    private final Map<Task<?>, TaskCompletion> taskCompleted;

    public Board(long timeLimitInMinutes) {
        this.timeLimit = timeLimitInMinutes * 60000;
        this.tasks = Lists.newArrayList();
        this.taskCompleted = Maps.newHashMap();

        generateTasks();
    }

    public void start() {
        startedAt = System.currentTimeMillis();
    }

    public List<Task<?>> getTasks() {
        return tasks;
    }

    public TaskCompletion getTaskCompletion(Task<?> task) {
        return taskCompleted.computeIfAbsent(task, TaskCompletion::new);
    }

    public void generateTasks() {
        for (int i = 0; i < 25; i++) {
            final int random = ThreadRandom.nextInt(5);

            switch (random) {
                case 0 -> tasks.add(new BreedAnimal(Type.BREED_ANIMAL.randomAllowed(), range(1, 4)));
                case 1 -> tasks.add(new CraftItem(Type.CRAFT_ITEM.randomAllowed(), range(1, 2)));
                case 2 -> tasks.add(new DieFromCause(Type.DIE_FROM_CAUSE.randomAllowed()));
                case 3 -> tasks.add(new GatherItem(Type.GATHER_ITEM.randomAllowed(), range(2, 6)));
                case 4 -> tasks.add(new SlayEntity(Type.KILL_ENTITY.randomAllowed(), range(1, 5)));
            }
        }
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
}
