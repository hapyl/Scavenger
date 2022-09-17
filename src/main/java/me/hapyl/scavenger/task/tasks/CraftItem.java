package me.hapyl.scavenger.task.tasks;

import me.hapyl.scavenger.game.Board;
import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.Type;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CraftItem extends Task<Material> {

    public CraftItem(Board board) {
        super(Type.CRAFT_ITEM, board, 1, 2);
    }

    @Override
    public ItemStack getMaterial() {
        return new ItemBuilder(getT()).setAmount(getAmount()).build();
    }

    @Override
    public void appendLore(ItemBuilder builder) {
        builder.addSmartLore(
                "Your team must craft an item to advance this task. Crafted item will be removed from your inventory.",
                "&8"
        );
        builder.addLore();
        builder.addLore("&7Item to craft &e&l" + Chat.capitalize(getT()));
        builder.addLore("&7Times to craft &e&l" + getAmount());
    }

}
