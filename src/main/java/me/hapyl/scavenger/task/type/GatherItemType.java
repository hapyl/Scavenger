package me.hapyl.scavenger.task.type;

import me.hapyl.scavenger.game.Board;
import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.tasks.GatherItem;
import me.hapyl.scavenger.translate.Translate;
import me.hapyl.spigotutils.module.util.Enums;
import org.bukkit.Material;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GatherItemType extends Type<Material> {

    public GatherItemType() {
        super(Translate.TASK_GATHER_ITEM);

        for (Material material : Material.values()) {
            if (material.isItem()) {
                final String materialName = material.name();
                if (!stringContainsAny(materialName, "SPAWN_EGG", "COMMAND_BLOCK")) {
                    addAllowed(material);
                }
            }
        }
    }

    @Nonnull
    @Override
    public Task<Material> createTask(@Nonnull Board board) {
        return new GatherItem(this, board);
    }

    @Nullable
    @Override
    public Material readType(@Nonnull String string) {
        return Enums.byName(Material.class, string);
    }

    @Nonnull
    @Override
    public String getPath() {
        return "gather_item";
    }
}
