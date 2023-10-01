package me.hapyl.scavenger.task.type;

import me.hapyl.scavenger.Main;
import me.hapyl.scavenger.game.Board;
import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.tasks.SlayEntity;
import me.hapyl.scavenger.translate.Translate;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class KillEntityType extends Type<EntityType> {
    public KillEntityType() {
        super(Translate.TASK_KILL_ENTITY);

        loadAllowedFromConfig();
    }

    @Nullable
    @Override
    public EntityType readType(@Nonnull String string) {
        for (EntityType type : EntityType.values()) {
            if (!type.name().equalsIgnoreCase(string)) {
                continue;
            }

            if (!type.isAlive()) {
                throw Main.errorFailedToLoad(this, "Entity is not alive! " + string);
            }

            return type;
        }

        return null;
    }

    @Nonnull
    @Override
    public String getPath() {
        return "kill_entity";
    }

    @Nonnull
    @Override
    public Task<EntityType> createTask(@Nonnull Board board) {
        return new SlayEntity(this, board);
    }
}
