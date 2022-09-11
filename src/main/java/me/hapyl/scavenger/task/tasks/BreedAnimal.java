package me.hapyl.scavenger.task.tasks;

import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.Type;
import me.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.entity.EntityType;

public class BreedAnimal extends Task<EntityType> {

    public BreedAnimal(EntityType type, int amount) {
        super(Type.BREED_ANIMAL, type, amount);
    }

}
