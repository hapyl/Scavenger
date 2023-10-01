package me.hapyl.scavenger.task;

import com.google.common.collect.Lists;
import me.hapyl.scavenger.game.Board;
import me.hapyl.scavenger.game.BoardSettings;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    private final List<Task<?>> pinnedTasks;
    private final BoardSettings boardSettings;

    public PlayerData(@Nonnull UUID uuid, @Nonnull Board board) {
        this.uuid = uuid;
        this.pinnedTasks = Lists.newArrayList();
        this.boardSettings = new BoardSettings(board);
    }

    @Nonnull
    public List<Task<?>> getPinnedTasks() {
        return pinnedTasks;
    }

    @Nonnull
    public BoardSettings getSettings() {
        return boardSettings;
    }
}
