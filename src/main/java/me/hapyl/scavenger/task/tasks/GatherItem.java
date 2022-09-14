package me.hapyl.scavenger.task.tasks;

import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.Type;
import me.hapyl.spigotutils.module.chat.Chat;
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

    @Override
    public void appendLore(ItemBuilder builder) {
        builder.addSmartLore(
                "Your team must gather an item to advance this task. Gathered item will be removed from your inventory.",
                "&8"
        );
        builder.addLore();
        builder.addLore("&7Item to gather &a&l" + Chat.capitalize(getT()));
        builder.addLore("&7Times to kill &a&l" + getAmount());
    }
}
