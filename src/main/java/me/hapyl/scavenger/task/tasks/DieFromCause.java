package me.hapyl.scavenger.task.tasks;

import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.Type;
import me.hapyl.scavenger.utils.WrittenTextureValues;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class DieFromCause extends Task<EntityDamageEvent.DamageCause> {

    public DieFromCause(EntityDamageEvent.DamageCause cause) {
        super(Type.DIE_FROM_CAUSE, cause, 1);
    }

    @Override
    public ItemStack getMaterial() {
        return new ItemStack(WrittenTextureValues.damageCauseTexture.getOrDefault(getT(), Material.BEDROCK));
    }

    @Override
    public void appendLore(ItemBuilder builder) {
        builder.addSmartLore("Your team must die from certain cause to advance this task.", "&8");
        builder.addLore();
        builder.addLore("&7Die from &b&l" + Chat.capitalize(getT()));
        builder.addLore("&7Times to die &b&l" + getAmount());
    }
}
