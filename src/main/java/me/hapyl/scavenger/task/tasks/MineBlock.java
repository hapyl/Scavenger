package me.hapyl.scavenger.task.tasks;

import me.hapyl.scavenger.game.Board;
import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.type.Type;
import me.hapyl.scavenger.translate.Translate;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class MineBlock extends Task<Material> {

    public MineBlock(@Nonnull Type<Material> type, @Nonnull Board board) {
        super(type, board);
    }

    @Override
    public ItemStack getMaterial() {
        return new ItemBuilder(getItem()).build();
    }

    @Nonnull
    @Override
    public String formatItem() {
        return Chat.capitalize(getItem());
    }

    @Nonnull
    @Override
    public Translate getDescription() {
        return Translate.TASK_MINE_BLOCK_ABOUT;
    }

    @Nonnull
    @Override
    public Translate getTypeDescription() {
        return Translate.TASK_MINE_BLOCK_TYPE;
    }

    @Nonnull
    @Override
    public Translate getAmountDescription() {
        return Translate.TASK_MINE_BLOCK_AMOUNT;
    }
}
