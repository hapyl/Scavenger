package me.hapyl.scavenger.game;

import me.hapyl.scavenger.Main;
import me.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerHandler implements Listener {

    @EventHandler()
    public void handlePlayerJoin(PlayerJoinEvent ev) {
        final Player player = ev.getPlayer();
        Main.getUIManager().createScoreboard(player);
        ev.setJoinMessage(Chat.format("&7[&a+&7] %s%s &bjoined.", player.isOp() ? "&c" : "&b", player.getName()));
    }

    @EventHandler()
    public void handlePlayerLeave(PlayerQuitEvent ev) {
        final Player player = ev.getPlayer();
        Chat.format("&7[&c-&7] %s%s &bleft.", player.isOp() ? "&c" : "&b", player.getName());
    }

    @EventHandler()
    public void handleAsyncChat(AsyncPlayerChatEvent ev) {

    }

}
