package me.hapyl.scavenger.game;

import me.hapyl.scavenger.Inject;
import me.hapyl.scavenger.Main;
import me.hapyl.scavenger.task.LinkedType;
import me.hapyl.spigotutils.module.util.WeightedCollection;

import javax.annotation.Nullable;

public class Manager extends Inject {

    private Board board;
    private final WeightedCollection<LinkedType> weight = new WeightedCollection<>();

    public Manager(Main main) {
        super(main);

        // Populate weighted map
        weight.add(LinkedType.GATHER_ITEM, 30);
        weight.add(LinkedType.CRAFT_ITEM, 20);
        weight.add(LinkedType.BREED_ANIMAL, 10);
        weight.add(LinkedType.DIE_FROM_CAUSE, 15);
        weight.add(LinkedType.DIE_FROM_ENTITY, 15);
        weight.add(LinkedType.KILL_ENTITY, 10);
    }

    @Nullable
    public Board getBoard() {
        return board;
    }

    public WeightedCollection<LinkedType> getWeight() {
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
