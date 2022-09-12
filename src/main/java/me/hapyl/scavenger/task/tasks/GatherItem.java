package me.hapyl.scavenger.task.tasks;

import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.Type;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GatherItem extends Task<Material> {

    public GatherItem(Material material, int amount) {
        super(Type.GATHER_ITEM, material, amount);
    }

    @Override
    public ItemStack getMaterial() {
        return new ItemBuilder(getT()).setAmount(getAmount()).build();
    }
}
