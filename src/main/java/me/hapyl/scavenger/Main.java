package me.hapyl.scavenger;

import me.hapyl.scavenger.commands.ScavengerCommand;
import me.hapyl.scavenger.commands.TeamCommand;
import me.hapyl.scavenger.task.Handler;
import me.hapyl.spigotutils.module.command.CommandProcessor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main plugin;

    private Manager manager;

    @Override
    public void onEnable() {
        plugin = this;

        this.manager = new Manager(this);

        // Reg commands
        final CommandProcessor processor = new CommandProcessor(this);
        processor.registerCommand(new TeamCommand("team"));
        processor.registerCommand(new ScavengerCommand("scavenger"));

        final PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new Handler(), this);

    }

    public static Manager getManager() {
        return getPlugin().manager;
    }

    public static Main getPlugin() {
        return plugin;
    }
}
