package me.hapyl.scavenger;

import me.hapyl.scavenger.commands.ScavengerCommand;
import me.hapyl.scavenger.commands.StuckCommand;
import me.hapyl.scavenger.commands.TeamCommand;
import me.hapyl.scavenger.game.BingoWorldHandler;
import me.hapyl.scavenger.game.GameLoop;
import me.hapyl.scavenger.game.Manager;
import me.hapyl.scavenger.game.PlayerHandler;
import me.hapyl.scavenger.scoreboard.UIManager;
import me.hapyl.scavenger.task.Handler;
import me.hapyl.spigotutils.EternaAPI;
import me.hapyl.spigotutils.module.command.CommandProcessor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public final class Main extends JavaPlugin {

    private static Main plugin;

    private Manager manager;
    private UIManager uiManager;

    @Override
    public void onEnable() {
        plugin = this;

        // Init API
        new EternaAPI(this);

        // Init managers
        this.manager = new Manager(this);
        this.uiManager = new UIManager(this);

        getServer().setIdleTimeout(0);

        // Reg commands
        final CommandProcessor processor = new CommandProcessor(this);
        processor.registerCommand(new TeamCommand("team"));
        processor.registerCommand(new ScavengerCommand("scavenger"));
        processor.registerCommand(new StuckCommand("stuck"));

        // Reg events
        new Handler(this);
        new PlayerHandler(this);
        new BingoWorldHandler(this);

        final BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskTimer(this, new GameLoop(this), 0L, 0L);

    }

    public UIManager getUIManager() {
        return uiManager;
    }

    public Manager getManager() {
        return manager;
    }

    public static Main getPlugin() {
        return plugin;
    }
}
