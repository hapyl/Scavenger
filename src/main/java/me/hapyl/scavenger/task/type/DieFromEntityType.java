package me.hapyl.scavenger.task.type;

import me.hapyl.scavenger.game.Board;
import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.tasks.DieFromEntity;
import me.hapyl.scavenger.translate.Translate;
import org.bukkit.entity.Enemy;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;

import javax.annotation.Nonnull;

public class DieFromEntityType extends Type<EntityType> {
    public DieFromEntityType() {
        super(Translate.TASK_DIE_FROM_ENTITY);

        for (EntityType type : EntityType.values()) {
            if (!isEntityValid(type)) {
                continue;
            }

            final Class<? extends Entity> entityClass = type.getEntityClass();

            if (entityClass != Enemy.class) {
                continue;
            }

            addAllowed(type);
        }
    }

    @Nonnull
    @Override
    public Task<EntityType> createTask(@Nonnull Board board) {
        return new DieFromEntity(this, board);
    }

    @Nonnull
    @Override
    public String getPath() {
        return "die_from_entity";
    }
}
