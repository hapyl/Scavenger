package me.hapyl.scavenger.task.tasks;

import me.hapyl.scavenger.game.Board;
import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.Type;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementDisplay;
import org.bukkit.inventory.ItemStack;

public class AdvanceAdvancement extends Task<Advancement> {
    public AdvanceAdvancement(Board board) {
        super(Type.ADVANCEMENT_ADVANCER, board, 1, 1);
    }

    @Override
    public ItemStack getMaterial() {
        return new ItemBuilder(Material.FLOWER_BANNER_PATTERN).build();
    }

    @Override
    public void appendLore(ItemBuilder builder) {
        final Advancement advancement = getT();
        builder.addSmartLore("Your team must complete advancement to advance this task.", "&8");
        builder.addLore();

        if (advancement == null) {
            return;
        }

        final AdvancementDisplay display = advancement.getDisplay();
        if (display == null) {
            return;
        }

        //        builder.addLore("&7Advancement to complete &b&l" + Chat.capitalize(display.getTitle()));
        //        builder.addSmartLore(display.getDescription(), " &8");
        builder.addLore("&7Times to complete &b&l" + getAmount());
    }
}
