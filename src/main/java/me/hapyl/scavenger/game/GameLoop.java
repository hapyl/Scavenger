package me.hapyl.scavenger.game;

import me.hapyl.scavenger.Main;

public class GameLoop implements Runnable {

    private int tick;

    @Override
    public void run() {

        // Every second loop
        if (tick % 20 == 0) {
            Main.getUIManager().updateAll();
        }

        ++tick;
    }

}
