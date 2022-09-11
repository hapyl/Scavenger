package me.hapyl.scavenger.task.tasks;

import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.Type;
import me.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.Material;

public class GatherItem extends Task<Material> {

    public GatherItem(Material material, int amount) {
        super(Type.GATHER_ITEM, material, amount);
    }
}
