package me.hapyl.scavenger.task.tasks;

import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.Type;
import me.hapyl.scavenger.utils.WrittenTextureValues;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class DieFromEntity extends Task<EntityType> {
    public DieFromEntity(EntityType entityType, int amount) {
        super(Type.DIE_FROM_ENTITY, entityType, amount);
    }

    @Override
    public ItemStack getMaterial() {
        return ItemBuilder.playerHeadUrl(WrittenTextureValues.entityTexture.getOrDefault(getT(), "")).build();
    }

    @Override
    public void appendLore(ItemBuilder builder) {
        builder.addSmartLore("Your team must die from certain mob to advance this task.", "&8");
        builder.addLore();
        builder.addLore("&7Die from &b&l" + Chat.capitalize(getT()));
        builder.addLore("&7Times to die &b&l" + getAmount());
    }
}
