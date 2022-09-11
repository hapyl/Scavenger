package me.hapyl.scavenger.task.tasks;

import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.Type;
import me.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.Material;

public class CraftItem extends Task<Material> {

    public CraftItem(Material material, int amount) {
        super(Type.CRAFT_ITEM, material, amount);
    }

}
