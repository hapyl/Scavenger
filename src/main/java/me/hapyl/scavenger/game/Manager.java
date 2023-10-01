package me.hapyl.scavenger.game;

import me.hapyl.scavenger.Inject;
import me.hapyl.scavenger.Main;
import me.hapyl.scavenger.task.type.Type;
import me.hapyl.scavenger.task.type.Types;
import me.hapyl.spigotutils.module.util.WeightedCollection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Manager extends Inject {

    private Board board;
    private final WeightedCollection<Type<?>> weight = new WeightedCollection<>();

    public Manager(Main main) {
        super(main);

        // Populate a weighted map
        weight.add(Types.MINE_BLOCK, 100);

        weight.add(Types.GATHER_ITEM, 30);
        weight.add(Types.CRAFT_ITEM, 20);
        weight.add(Types.BREED_ANIMAL, 10);
        weight.add(Types.DIE_FROM_CAUSE, 15);
        weight.add(Types.DIE_FROM_ENTITY, 15);
        weight.add(Types.KILL_ENTITY, 10);
    }

    @Nullable
    public Board getBoard() {
        return board;
    }

    @Nonnull
    public WeightedCollection<Type<?>> getWeight() {
        return weight;
    }

    public void createBoard(int minutes) {
        board = new Board(this, minutes == 0 ? 30 : minutes);
    }

    public void removeBoard() {
        board.stop();
        board = null;
    }
}
