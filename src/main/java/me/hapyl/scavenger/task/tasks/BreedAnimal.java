package me.hapyl.scavenger.task.tasks;

import me.hapyl.scavenger.game.Board;
import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.Type;
import me.hapyl.scavenger.utils.WrittenTextureValues;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class BreedAnimal extends Task<EntityType> {

    public BreedAnimal(Board board) {
        super(Type.BREED_ANIMAL, board, 1, 2);
    }

    @Override
    public ItemStack getMaterial() {
        return ItemBuilder.playerHeadUrl(WrittenTextureValues.entityTexture.get(getT())).build();
    }

    @Override
    public void appendLore(ItemBuilder builder) {
        builder.addSmartLore("Your team must breed animals to advance this task.", "&8");
        builder.addLore();
        builder.addLore("&7Animals to breed &b&l" + Chat.capitalize(getT()));
        builder.addLore("&7Times to breed &b&l" + getAmount());
    }

}
