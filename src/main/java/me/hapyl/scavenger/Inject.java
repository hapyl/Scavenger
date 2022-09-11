package me.hapyl.scavenger;

public class Inject {

    private final Main main;

    public Inject(Main main) {
        this.main = main;
    }

    public Main getPlugin() {
        return main;
    }
}
