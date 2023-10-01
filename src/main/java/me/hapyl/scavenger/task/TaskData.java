package me.hapyl.scavenger.task;

import javax.annotation.Nonnull;

public class TaskData {

    private final Task<?> task;
    private final TaskCompletion taskCompletion;

    public TaskData(@Nonnull Task<?> task) {
        this.task = task;
        this.taskCompletion = new TaskCompletion(task);
    }

    @Nonnull
    public TaskCompletion getTaskCompletion() {
        return taskCompletion;
    }

    @Nonnull
    public Task<?> getTask() {
        return task;
    }
}
