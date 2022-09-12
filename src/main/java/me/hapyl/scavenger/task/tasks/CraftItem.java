package me.hapyl.scavenger.task.tasks;

import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.Type;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CraftItem extends Task<Material> {

    public CraftItem(Material material, int amount) {
        super(Type.CRAFT_ITEM, material, amount);
    }

    @Override
    public ItemStack getMaterial() {
        return new ItemBuilder(getT()).setAmount(getAmount()).build();
    }

}
