package me.hapyl.scavenger.game;

import me.hapyl.scavenger.Inject;
import me.hapyl.scavenger.Main;

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

    public void removeBoard() {
        board.stop();
        board = null;
    }
}
