package me.hapyl.scavenger.gui;

import me.hapyl.scavenger.Message;
import me.hapyl.scavenger.game.Team;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.inventory.gui.PlayerGUI;
import me.hapyl.spigotutils.module.player.PlayerLib;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Set;

public class TeamGUI extends PlayerGUI {
    public TeamGUI(Player player) {
        super(player, "Team Selection", 3);
        updateInventory();
    }

    private void updateInventory() {
        clearClickEvents();

        int slot = 10;
        for (Team team : Team.values()) {
            final ItemBuilder builder = new ItemBuilder(team.getMaterial()).setName(team.getName());
            final boolean isTeam = team.isPlayer(getPlayer());

            builder.addLore("");
            builder.addLore("&7Players:");
            final Set<Player> players = team.getPlayers();
            if (players.isEmpty()) {
                builder.addLore("&8Empty!");
            }
            else {
                for (Player player : players) {
                    builder.addLore(" %s• %s", team.getColor(), player.getName());
                }
            }

            builder.addLore();
            if (team.isFull()) {
                builder.addLore("&cTeam is full!");
                setClick(slot, player -> {
                    Message.sendMessage(player, "&cThis team is full!");
                    PlayerLib.playSound(player, Sound.ENTITY_VILLAGER_NO, 1.0f);
                    updateInventory();
                });
            }
            else {
                if (isTeam) {
                    builder.addLore("&aYou are in this team!");
                }
                else {
                    builder.addLore("&eClick to join");
                    setClick(slot, player -> {
                        team.addToTeam(player);
                        PlayerLib.plingNote(player, 2.0f);
                        updateInventory();
                    });
                }
            }

            setItem(slot, builder.predicate(isTeam, ItemBuilder::glow).build());
            slot += 2;
        }

        openInventory();
    }
}
