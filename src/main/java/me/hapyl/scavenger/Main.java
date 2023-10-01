package me.hapyl.scavenger;

import me.hapyl.scavenger.commands.*;
import me.hapyl.scavenger.commands.test.TestAdvancement;
import me.hapyl.scavenger.game.BingoWorldHandler;
import me.hapyl.scavenger.game.GameLoop;
import me.hapyl.scavenger.game.Manager;
import me.hapyl.scavenger.game.PlayerHandler;
import me.hapyl.scavenger.scoreboard.UIManager;
import me.hapyl.scavenger.task.Handler;
import me.hapyl.scavenger.task.type.Type;
import me.hapyl.scavenger.task.type.Types;
import me.hapyl.scavenger.translate.Translation;
import me.hapyl.spigotutils.EternaAPI;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.CommandProcessor;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import javax.annotation.Nonnull;

public final class Main extends JavaPlugin {

    private static Main plugin;

    private Manager manager;
    private UIManager uiManager;
    private Translation translation;

    @Override
    public void onEnable() {
        plugin = this;

        // Init API
        new EternaAPI(this);

        // Init managers
        this.manager = new Manager(this);
        this.uiManager = new UIManager(this);
        this.translation = new Translation(this);

        // Load config
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        // Reg commands
        final CommandProcessor processor = new CommandProcessor(this);
        processor.registerCommand(new TeamCommand("team"));
        processor.registerCommand(new ScavengerCommand("scavenger"));
        processor.registerCommand(new StuckCommand("stuck"));
        processor.registerCommand(new LanguageCommand("language"));
        processor.registerCommand(new ReloadLanguageCommand("reloadlangconfig"));

        // Test commands
        processor.registerCommand(new TestAdvancement("testadvancement"));
        processor.registerCommand(new SimplePlayerAdminCommand("writeConfigValues") {
            @Override
            protected void execute(Player player, String[] args) {
                for (Type<?> type : Types.values()) {
                    getConfig().set(type.getFullPath() + "test", true);
                }

                Chat.sendMessage(player, "&aDone!");
                saveConfig();
            }
        });


        // Reg events
        new Handler(this);
        new PlayerHandler(this);
        new BingoWorldHandler(this);

        getServer().setIdleTimeout(0);

        final BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskTimer(this, new GameLoop(this), 0L, 0L);

        scheduler.runTaskLater(this, () -> {
            final Server server = getServer();
            server.setIdleTimeout(0);

            for (Player player : Bukkit.getOnlinePlayers()) {
                translation.loadPlayerLangFromNbt(player);
            }
        }, 20L);

    }

    @Nonnull
    public Translation getTranslate() {
        return translation;
    }

    @Nonnull
    public UIManager getUIManager() {
        return uiManager;
    }

    @Nonnull
    public Manager getManager() {
        return manager;
    }

    public static Main getPlugin() {
        return plugin;
    }

    public static RuntimeException errorFailedToLoad(Type<?> type, String s) {
        return error("Failed to load \"" + type.getPath() + "\"! " + s);
    }

    public static RuntimeException error(String s) {
        final RuntimeException exception = new RuntimeException(s);

        plugin.getLogger().severe(s);
        Bukkit.getPluginManager().disablePlugin(plugin);

        return exception;
    }
}
