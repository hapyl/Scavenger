package me.hapyl.scavenger.task.tasks;

import me.hapyl.scavenger.game.Board;
import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.Type;
import me.hapyl.scavenger.utils.WrittenTextureValues;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class SlayEntity extends Task<EntityType> {

    public SlayEntity(Board board) {
        super(Type.KILL_ENTITY, board, 1, 1);
    }

    @Override
    public ItemStack getMaterial() {
        return ItemBuilder.playerHeadUrl(WrittenTextureValues.entityTexture.getOrDefault(getT(), "")).build();
    }

    @Override
    public void appendLore(ItemBuilder builder) {
        builder.addSmartLore("Your team must kill a mob to advance this task.", "&8");
        builder.addLore();
        builder.addLore("&7Mob to kill &c&l" + Chat.capitalize(getT()));
        builder.addLore("&7Times to kill &c&l" + getAmount());
    }
}
