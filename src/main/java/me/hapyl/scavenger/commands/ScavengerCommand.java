package me.hapyl.scavenger.commands;

import me.hapyl.scavenger.Main;
import me.hapyl.scavenger.game.Board;
import me.hapyl.scavenger.game.Manager;
import me.hapyl.scavenger.game.Team;
import me.hapyl.scavenger.gui.BoardGUI;
import me.hapyl.scavenger.task.Type;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;

public class ScavengerCommand extends SimplePlayerAdminCommand {
    public ScavengerCommand(String name) {
        super(name);
        setAliases("s");
    }

    @Override
    protected void execute(Player player, String[] args) {
        final Manager manager = Main.getManager();

        if (args.length >= 1) {
            final String firstArg = args[0].toLowerCase();

            switch (firstArg) {
                case "new" -> {
                    if (!Team.isAtLeastOneTeam()) {
                        Chat.sendMessage(player, "&cMust be at least one team!");
                        return;
                    }
                    Chat.broadcast("&eGenerating new board...");
                    manager.createBoard();
                    Chat.broadcast("&aGenerated new board!");
                }

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
                    final Type<?> type = Type.byName.get(args[1].toUpperCase(Locale.ROOT));
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
            return completerSort(new String[] { "new", "values" }, args);
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("values")) {
            return completerSort(Type.byName.keySet(), args);
        }
        return null;
    }
}
