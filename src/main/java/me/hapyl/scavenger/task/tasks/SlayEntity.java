package me.hapyl.scavenger.task.tasks;

import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.Type;
import me.hapyl.scavenger.utils.WrittenTextureValues;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class SlayEntity extends Task<EntityType> {

    public SlayEntity(EntityType type, int amount) {
        super(Type.KILL_ENTITY, type, amount);
    }

    @Override
    public ItemStack getMaterial() {
        return ItemBuilder.playerHeadUrl(WrittenTextureValues.entityTexture.getOrDefault(getT(), "")).build();
    }

}
