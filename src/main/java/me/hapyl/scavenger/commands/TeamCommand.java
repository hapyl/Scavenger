package me.hapyl.scavenger.commands;

import me.hapyl.scavenger.Team;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.SimplePlayerCommand;
import me.hapyl.spigotutils.module.util.Validate;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TeamCommand extends SimplePlayerCommand {
    public TeamCommand(String name) {
        super(name);
    }

    @Override
    protected void execute(Player player, String[] args) {
        // team
        // team join (Team)
        // team leave (Team)

        if (args.length == 0) {
            Chat.sendMessage(player, "menu not ready yet");
            return;
        }

        if (args.length >= 2) {
            final String firstArg = args[0];
            final Team team = Validate.getEnumValue(Team.class, args[1]);

            if (team == null) {
                Chat.sendMessage(player, "&cInvalid team!");
                return;
            }

            if (firstArg.equalsIgnoreCase("join")) {
                if (team.isFull()) {
                    Chat.sendMessage(player, "&cThis team is full!");
                    return;
                }

                if (team.isPlayer(player)) {
                    Chat.sendMessage(player, "&cYou are already in this team!");
                    return;
                }

                final Team oldTeam = Team.getTeam(player);
                if (oldTeam != null) {
                    oldTeam.removePlayer(player);
                }
                team.addPlayer(player);
            }

            else if (firstArg.equalsIgnoreCase("leave")) {
                if (team.isPlayer(player)) {
                    Chat.sendMessage(player, "&cYou are not in a team!");
                    return;
                }

                team.removePlayer(player);
            }

        }
    }

    @Override
    protected List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return completerSort(new String[] { "join", "leave" }, args);
        }
        else if (args.length == 2) {
            return completerSort(arrayToList(Team.values()), args);
        }
        return null;
    }
}
