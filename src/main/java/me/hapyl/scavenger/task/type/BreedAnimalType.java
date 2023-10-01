package me.hapyl.scavenger.task.type;

import me.hapyl.scavenger.Main;
import me.hapyl.scavenger.game.Board;
import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.tasks.BreedAnimal;
import me.hapyl.scavenger.translate.Translate;
import me.hapyl.spigotutils.module.util.Enums;
import org.bukkit.entity.Breedable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BreedAnimalType extends Type<EntityType> {
    public BreedAnimalType() {
        super(Translate.TASK_BREED_ANIMAL);

        loadAllowedFromConfig();
    }

    @Nonnull
    @Override
    public Task<EntityType> createTask(@Nonnull Board board) {
        return new BreedAnimal(this, board);
    }

    @Nullable
    @Override
    public EntityType readType(@Nonnull String string) {
        for (EntityType type : EntityType.values()) {
            if (type.name().equalsIgnoreCase(string)) {
                final Class<? extends Entity> entityClass = type.getEntityClass();

                if (entityClass == null) {
                    throw Main.errorFailedToLoad(this, "Entity class is null! " + string);
                }

                if (!Breedable.class.isAssignableFrom(entityClass)) {
                    throw Main.errorFailedToLoad(this, "Entity is not breedable! " + string);
                }

                return type;
            }
        }

        return null;
    }

    @Nonnull
    @Override
    public String getPath() {
        return "breed_animal";
    }
}
