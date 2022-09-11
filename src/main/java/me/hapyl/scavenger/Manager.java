package me.hapyl.scavenger;

import javax.annotation.Nullable;

public class Manager extends Inject {

    private Board board;

    public Manager(Main main) {
        super(main);
    }

    @Nullable
    public Board getBoard() {
        return board;
    }

    public void createBoard() {
        board = new Board(30);
    }
}
