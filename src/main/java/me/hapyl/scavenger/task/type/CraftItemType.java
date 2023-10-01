package me.hapyl.scavenger.task.type;

import me.hapyl.scavenger.game.Board;
import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.tasks.CraftItem;
import me.hapyl.scavenger.translate.Translate;
import org.bukkit.Material;

import javax.annotation.Nonnull;

public class CraftItemType extends Type<Material> {

    public CraftItemType() {
        super(Translate.TASK_CRAFT_ITEM);

        for (Material material : Material.values()) {
            if (!isUsedInRecipe(material) || !isMaterialValid(material)) {
                continue;
            }

            switch (material) {
                case CRAFTING_TABLE -> {
                }
                default -> addAllowed(material);
            }
        }

    }

    @Nonnull
    @Override
    public Task<Material> createTask(@Nonnull Board board) {
        return new CraftItem(this, board);
    }

    @Nonnull
    @Override
    public String getPath() {
        return "craft_item";
    }

}
