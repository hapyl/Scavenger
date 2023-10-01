package me.hapyl.scavenger.commands;

import com.google.common.collect.Sets;
import me.hapyl.scavenger.Main;
import me.hapyl.scavenger.game.BingoWorld;
import me.hapyl.scavenger.game.Board;
import me.hapyl.scavenger.game.Manager;
import me.hapyl.scavenger.game.Team;
import me.hapyl.scavenger.gui.BoardGUI;
import me.hapyl.scavenger.task.type.Type;
import me.hapyl.scavenger.task.type.Types;
import me.hapyl.scavenger.utils.FileUtils;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ScavengerCommand extends SimplePlayerAdminCommand {
    public ScavengerCommand(String name) {
        super(name);
        setAliases("s");
    }

    @Override
    protected void execute(Player player, String[] args) {
        final Manager manager = Main.getPlugin().getManager();

        if (args.length >= 1) {
            final String firstArg = args[0].toLowerCase();

            switch (firstArg) {
                case "new" -> {
                    if (!Team.isAtLeastOneTeam()) {
                        Chat.sendMessage(player, "&cMust be at least one team!");
                        return;
                    }

                    final int minutes = args.length >= 2 ? Validate.getInt(args[1]) : 0;
                    manager.createBoard(minutes);
                }

                case "deleteworld" -> {
                    if (args.length != 2) {
                        Chat.sendMessage(player, "&cMust provide world name.");
                        return;
                    }

                    final String name = args[1];

                    if (!name.contains("bingo_")) {
                        Chat.sendMessage(player, "&c%s is not a bingo world!", name);
                        return;
                    }

                    World world = Bukkit.getWorld(name);
                    File file = null;

                    // Try to load world if it's unloaded
                    if (world == null) {
                        file = new File(Bukkit.getWorldContainer(), name);
                        if (!file.exists()) {
                            Chat.sendMessage(player, "&cThis world doesn't exist!");
                            return;
                        }
                    }
                    else {
                        // Evacuate players
                        final Location spawnLocation = Bukkit.getWorlds().get(0).getSpawnLocation();
                        world.getPlayers().forEach(wp -> wp.teleport(spawnLocation));
                    }

                    // Try deleting world
                    final boolean success = FileUtils.deleteFolder(file);

                    if (success) {
                        Chat.sendMessage(player, "&aBingo world '%s' was successfully deleted!", name);
                    }
                    else {
                        Chat.sendMessage(player, "&cUnable to delete '%s'!", name);
                    }
                }

                case "deleteallbingoworlds" -> {
                    final File container = Bukkit.getWorldContainer();
                    final File[] files = container.listFiles();

                    if (files == null || files.length == 0) {
                        Chat.sendMessage(player, "&cDirectory is empty!");
                        return;
                    }

                    int deleted = 0;
                    for (File file : files) {
                        if (!file.isDirectory()) {
                            continue;
                        }

                        final String fileName = file.getName();
                        if (!fileName.startsWith("bingo_")) {
                            continue;
                        }

                        FileUtils.deleteFolder(file);
                        ++deleted;
                    }

                    Chat.sendMessage(player, "&aDeleted %s bingo worlds!", deleted);
                }

                case "testworldcreation" -> new BingoWorld();

                case "stop" -> {
                    final Board board = manager.getBoard();
                    if (board == null) {
                        Chat.sendMessage(player, "&cThere is no active game!");
                        return;
                    }

                    Chat.sendMessage(player, "&aStopped the game.");
                    manager.removeBoard();
                }

                case "values" -> {
                    if (args.length != 2) {
                        Chat.sendMessage(player, "&cExpecting &4Type<?> &cas &4arg[1]&c, found nothing.");
                        return;
                    }

                    final Type<?> type = Types.byName(args[1].toUpperCase(Locale.ROOT));

                    if (type == null) {
                        Chat.sendMessage(player, "&cInvalid type!");
                        return;
                    }

                    Chat.sendMessage(player, "&b%s &aaccepts &b%s &atype.", type.getName(), type.getAccepting());
                    Chat.sendMessage(player, "&aWhich includes these values:");
                    final StringBuilder builder = new StringBuilder();
                    for (Object obj : type.getAllowed()) {
                        builder.append("&e").append(obj.toString().toLowerCase(Locale.ROOT)).append("&7, ");
                    }

                    Chat.sendMessage(player, builder.substring(0, builder.lastIndexOf(",") - 2) + "&7.");

                }
            }
            return;
        }

        new BoardGUI(player);
    }

    @Override
    protected List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return completerSort(new String[] { "new", "stop", "values", "testworldcreation", "deleteworld", "deleteallbingoworlds" }, args);
        }
        else {
            if (args.length == 2) {
                final String arg = args[0].toLowerCase();
                switch (arg) {
                    case "values" -> {
                        return completerSort(Types.names(), args);
                    }
                    case "deleteworld" -> {
                        final Set<String> names = Sets.newHashSet();
                        for (World world : Bukkit.getWorlds()) {
                            final String worldName = world.getName();
                            if (worldName.contains("bingo_")) {
                                names.add(worldName);
                            }
                        }

                        return completerSort(names, args);
                    }
                }
            }
        }
        return null;
    }

}
