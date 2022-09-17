package me.hapyl.scavenger.game;

import me.hapyl.scavenger.InjectListener;
import me.hapyl.scavenger.Main;
import me.hapyl.scavenger.utils.ScavengerItem;
import me.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public class PlayerHandler extends InjectListener {

    public PlayerHandler(Main main) {
        super(main);
    }

    @EventHandler()
    public void handleBlockPlace(BlockPlaceEvent ev) {
        final ItemStack item = ev.getItemInHand();

        if (!ScavengerItem.ITEM_BOARD_SHORTCUT.compare(item)) {
            return;
        }

        ev.setCancelled(true);
        ev.setBuild(false);
    }

    @EventHandler()
    public void handleItemDrop(PlayerDropItemEvent ev) {
        final ItemStack item = ev.getItemDrop().getItemStack();

        if (!ScavengerItem.ITEM_BOARD_SHORTCUT.compare(item)) {
            return;
        }

        ev.setCancelled(true);
    }

    @EventHandler()
    public void handleSwapItem(PlayerSwapHandItemsEvent ev) {
        final ItemStack item = ev.getOffHandItem();

        if (item == null || !ScavengerItem.ITEM_BOARD_SHORTCUT.compare(item)) {
            return;
        }

        ev.setCancelled(true);
    }

    @EventHandler()
    public void handleInventoryClick(InventoryClickEvent ev) {
        final ItemStack item = ev.getCurrentItem();

        if (item == null || !ScavengerItem.ITEM_BOARD_SHORTCUT.compare(item)) {
            return;
        }

        ev.setCancelled(true);
    }

    // Remove the board item.
    @EventHandler()
    public void handlePlayerDeathEvent(PlayerDeathEvent ev) {
        final Board board = getPlugin().getManager().getBoard();

        if (board == null) {
            return;
        }

        final ItemStack item = ScavengerItem.ITEM_BOARD_SHORTCUT.getItem();
        ev.getEntity().getInventory().remove(item);
        ev.getDrops().remove(item);
    }

    @EventHandler()
    public void handlePlayerRespawnEvent(PlayerRespawnEvent ev) {
        final Board board = getPlugin().getManager().getBoard();
        if (board == null) {
            return;
        }

        ScavengerItem.ITEM_BOARD_SHORTCUT.put(ev.getPlayer(), 8);
    }

    @EventHandler()
    public void handlePlayerJoin(PlayerJoinEvent ev) {
        final Player player = ev.getPlayer();
        Main.getPlugin().getUIManager().createScoreboard(player);
        ev.setJoinMessage(Chat.format("&7[&a+&7] %s%s &bjoined.", player.isOp() ? "&c" : "&b", player.getName()));
    }

    @EventHandler()
    public void handlePlayerLeave(PlayerQuitEvent ev) {
        final Player player = ev.getPlayer();
        Chat.format("&7[&c-&7] %s%s &bleft.", player.isOp() ? "&c" : "&b", player.getName());
    }

    @EventHandler()
    public void handleAsyncChat(AsyncPlayerChatEvent ev) {
        final Player player = ev.getPlayer();
        final Team team = Team.getTeam(player);
        final String message = ev.getMessage();

        ev.setCancelled(true);

        // Team message test
        if (message.startsWith("#") && team != null) {
            final String formatted = "&3&l(Team) %s%s&f:&o %s".formatted(team.getColor(), player.getName(), message.substring(1));
            team.getPlayers().forEach(tm -> Chat.sendMessage(tm, formatted));
            return;
        }

        // Yeah, not using formatting.
        Chat.broadcast(
                "&b%s%s&f: %s",
                team == null ? "" : (team.getNameCaps() + team.getColor() + " "),
                player.getDisplayName(),
                message
        );
    }

}
