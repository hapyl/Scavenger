package me.hapyl.scavenger.task.tasks;

import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.Type;
import org.bukkit.entity.EntityType;

public class SlayEntity extends Task<EntityType> {

    public SlayEntity(EntityType type, int amount) {
        super(Type.KILL_ENTITY, type, amount);
    }

}
