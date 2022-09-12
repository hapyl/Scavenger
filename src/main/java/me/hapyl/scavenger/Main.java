package me.hapyl.scavenger;

import me.hapyl.scavenger.commands.ScavengerCommand;
import me.hapyl.scavenger.commands.TeamCommand;
import me.hapyl.scavenger.game.GameLoop;
import me.hapyl.scavenger.game.Manager;
import me.hapyl.scavenger.scoreboard.UIManager;
import me.hapyl.scavenger.task.Handler;
import me.hapyl.spigotutils.EternaAPI;
import me.hapyl.spigotutils.module.command.CommandProcessor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public final class Main extends JavaPlugin {

    private static Main plugin;

    private Manager manager;
    private UIManager uiManager;

    @Override
    public void onEnable() {
        plugin = this;

        this.manager = new Manager(this);
        this.uiManager = new UIManager();
        new EternaAPI(this);

        // Reg commands
        final CommandProcessor processor = new CommandProcessor(this);
        processor.registerCommand(new TeamCommand("team"));
        processor.registerCommand(new ScavengerCommand("scavenger"));

        final PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new Handler(), this);

        final BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskTimer(this, new GameLoop(), 0L, 0L);

    }

    public static UIManager getUIManager() {
        return getPlugin().uiManager;
    }

    public static Manager getManager() {
        return getPlugin().manager;
    }

    public static Main getPlugin() {
        return plugin;
    }
}
