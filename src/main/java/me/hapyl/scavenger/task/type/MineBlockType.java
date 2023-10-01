package me.hapyl.scavenger.task.type;

import me.hapyl.scavenger.game.Board;
import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.tasks.MineBlock;
import me.hapyl.scavenger.translate.Translate;
import org.bukkit.Material;

import javax.annotation.Nonnull;

public class MineBlockType extends Type<Material> {
    public MineBlockType() {
        super(Translate.TASK_MINE_BLOCK);

        for (Material material : Material.values()) {
            if (material.isBlock()) {
                if (material.getBlastResistance() >= 600 || !isMaterialValid(material)) {
                    continue;
                }

                switch (material) {
                    case WATER, LAVA -> {
                    }
                    default -> {
                        addAllowed(material);
                    }
                }
            }
        }
    }

    @Nonnull
    @Override
    public Task<Material> createTask(@Nonnull Board board) {
        return new MineBlock(this, board);
    }

    @Nonnull
    @Override
    public String getPath() {
        return "mine_block";
    }
}
