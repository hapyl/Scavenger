package me.hapyl.scavenger;

import org.bukkit.event.Listener;

public class InjectListener extends Inject implements Listener {
    public InjectListener(Main main) {
        super(main);
        getPlugin().getServer().getPluginManager().registerEvents(this, main);
    }
}
