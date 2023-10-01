package me.hapyl.scavenger.task.tasks;

import me.hapyl.scavenger.game.Board;
import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.type.Type;
import me.hapyl.scavenger.translate.Translate;
import me.hapyl.scavenger.utils.WrittenTextureValues;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class BreedAnimal extends Task<EntityType> implements TranslateTask {

    public BreedAnimal(@Nonnull Type<EntityType> type, @Nonnull Board board) {
        super(type, board);
    }

    @Override
    public ItemStack getMaterial() {
        return ItemBuilder.playerHeadUrl(WrittenTextureValues.entityTexture.get(getItem())).build();
    }

    @Nonnull
    @Override
    public String formatItem() {
        return Chat.capitalize(getItem());
    }

    @Nonnull
    @Override
    public Translate getDescription() {
        return Translate.TASK_BREED_ANIMALS_ABOUT;
    }

    @Nonnull
    @Override
    public Translate getTypeDescription() {
        return Translate.TASK_BREED_ANIMALS_TYPE;
    }

    @Nonnull
    @Override
    public Translate getAmountDescription() {
        return Translate.TASK_BREED_ANIMALS_AMOUNT;
    }
}
