package me.hapyl.scavenger.game;

import me.hapyl.scavenger.Inject;
import me.hapyl.scavenger.Main;

public class GameLoop extends Inject implements Runnable {

    private int tick;

    public GameLoop(Main main) {
        super(main);
    }

    @Override
    public void run() {
        // Every second loop
        if (tick % 20 == 0) {

            // Game check
            final Manager manager = getPlugin().getManager();
            final Board board = manager.getBoard();

            if (board != null) {
                final long timeLeft = board.getTimeLeft();
                if (board.getStartedAt() != 0L && timeLeft < 0) {
                    manager.removeBoard();
                    return;
                }
            }

            // Update UI
            getPlugin().getUIManager().updateAll();
        }

        ++tick;
    }

}
