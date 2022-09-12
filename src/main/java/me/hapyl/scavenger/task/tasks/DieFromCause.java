package me.hapyl.scavenger.task.tasks;

import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.Type;
import me.hapyl.scavenger.utils.WrittenTextureValues;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class DieFromCause extends Task<EntityDamageEvent.DamageCause> {

    public DieFromCause(EntityDamageEvent.DamageCause cause) {
        super(Type.DIE_FROM_CAUSE, cause, 1);
    }

    @Override
    public ItemStack getMaterial() {
        return new ItemStack(WrittenTextureValues.damageCauseTexture.getOrDefault(getT(), Material.BEDROCK));
    }
}
