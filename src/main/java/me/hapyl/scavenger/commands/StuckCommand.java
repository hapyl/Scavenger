package me.hapyl.scavenger.commands;

import me.hapyl.scavenger.Main;
import me.hapyl.scavenger.Message;
import me.hapyl.scavenger.game.BingoWorld;
import me.hapyl.scavenger.game.Board;
import me.hapyl.scavenger.game.Manager;
import me.hapyl.spigotutils.module.command.SimplePlayerCommand;
import me.hapyl.spigotutils.module.player.PlayerLib;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class StuckCommand extends SimplePlayerCommand {
    public StuckCommand(String name) {
        super(name);
        setDescription("Use if stuck or in wrong world.");
    }

    @Override
    protected void execute(Player player, String[] args) {
        final Manager manager = Main.getPlugin().getManager();
        final Board board = manager.getBoard();

        if (board == null) {
            Message.sendMessage(player, "&cThere is no game!");
            return;
        }

        final BingoWorld world = board.getWorld();
        Message.sendMessage(player, "&eChecking if you're stuck...");

        if (player.getWorld() != world.getWorld()) {
            Message.sendMessage(player, "&aYou're stuck in another world!");
            PlayerLib.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 2.0f);
            player.teleport(world.getWorld().getSpawnLocation());
            return;
        }

        Message.sendMessage(player, "&cYou're not stuck!");

    }
}
