package me.hapyl.scavenger.task.tasks;

import me.hapyl.scavenger.game.Board;
import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.type.Type;
import me.hapyl.scavenger.translate.Translate;
import me.hapyl.scavenger.utils.WrittenTextureValues;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class DieFromCause extends Task<EntityDamageEvent.DamageCause> {

    public DieFromCause(Type<EntityDamageEvent.DamageCause> type, Board board) {
        super(type, board, type.getCountMin(), type.getCountMax());
    }

    @Override
    public ItemStack getMaterial() {
        return new ItemStack(WrittenTextureValues.damageCauseTexture.getOrDefault(getItem(), Material.BEDROCK));
    }

    @Nonnull
    @Override
    public String formatItem() {
        return Chat.capitalize(getItem());
    }

    @Nonnull
    @Override
    public Translate getDescription() {
        return Translate.TASK_DIE_FROM_CAUSE_ABOUT;
    }

    @Nonnull
    @Override
    public Translate getTypeDescription() {
        return Translate.TASK_DIE_FROM_CAUSE_TYPE;
    }

    @Nonnull
    @Override
    public Translate getAmountDescription() {
        return Translate.TASK_DIE_FROM_CAUSE_AMOUNT;
    }
}
