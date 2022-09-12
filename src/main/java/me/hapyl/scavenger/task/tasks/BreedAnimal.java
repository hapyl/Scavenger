package me.hapyl.scavenger.task.tasks;

import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.Type;
import me.hapyl.scavenger.utils.WrittenTextureValues;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class BreedAnimal extends Task<EntityType> {

    public BreedAnimal(EntityType type, int amount) {
        super(Type.BREED_ANIMAL, type, amount);
    }

    @Override
    public ItemStack getMaterial() {
        return ItemBuilder.playerHeadUrl(WrittenTextureValues.entityTexture.get(getT())).build();
    }
}
