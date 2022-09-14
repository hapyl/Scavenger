package me.hapyl.scavenger.game;

import me.hapyl.scavenger.InjectListener;
import me.hapyl.scavenger.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.WorldInitEvent;

public class BingoWorldHandler extends InjectListener {
    public BingoWorldHandler(Main main) {
        super(main);
    }

    @EventHandler()
    public void handleWorldInitEvent(WorldInitEvent ev) {
        ev.getWorld().setKeepSpawnInMemory(false);
    }
}
