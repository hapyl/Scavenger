package me.hapyl.scavenger.commands;

import me.hapyl.scavenger.Board;
import me.hapyl.scavenger.Main;
import me.hapyl.scavenger.Manager;
import me.hapyl.scavenger.Team;
import me.hapyl.scavenger.gui.BoardGUI;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import org.bukkit.entity.Player;

public class ScavengerCommand extends SimplePlayerAdminCommand {
    public ScavengerCommand(String name) {
        super(name);
        setAliases("s");
    }

    @Override
    protected void execute(Player player, String[] args) {
        final Manager manager = Main.getManager();

        if (args.length >= 1) {
            final String firstArg = args[0];
            if (firstArg.equalsIgnoreCase("new")) {
                if (!Team.isAtLeastOneTeam()) {
                    Chat.sendMessage(player, "&cMust be at least one team!");
                    return;
                }

                Chat.broadcast("&eGenerating new board...");
                manager.createBoard();
                Chat.broadcast("&aGenerated new board!");
            }
            return;
        }

        new BoardGUI(player);
    }
}
