package me.hapyl.scavenger.game;

import com.google.common.collect.Sets;
import me.hapyl.scavenger.Message;
import me.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.UUID;

public enum Team {

    RED(ChatColor.RED),
    GREEN(ChatColor.GREEN),
    BLUE(ChatColor.BLUE),
    PINK(ChatColor.LIGHT_PURPLE);

    private final ChatColor color;
    private final Set<UUID> players;

    Team(ChatColor color) {
        this.color = color;
        this.players = Sets.newHashSet();
    }

    public static boolean isAtLeastOneTeam() {
        for (Team value : values()) {
            if (!value.getPlayers().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public boolean isPlayer(Player player) {
        return players.contains(player.getUniqueId());
    }

    public void addPlayer(Player player) {
        players.add(player.getUniqueId());
        Message.broadcast("&a%s joined %s &ateam.", player.getName(), this.getName());
    }

    public boolean removePlayer(Player player) {
        Message.broadcast("&a%s left %s &ateam.", player.getName(), this.getName());
        return players.remove(player.getUniqueId());
    }

    public Set<UUID> getPlayers() {
        return players;
    }

    public boolean isFull() {
        return players.size() >= 4;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getName() {
        return color + Chat.capitalize(this) + "&7";
    }

    public String getNameCaps() {
        return color + "&l" + this.name() + "&7";
    }

    @Nullable
    public static Team getTeam(Player player) {
        for (Team value : values()) {
            if (value.isPlayer(player)) {
                return value;
            }
        }

        return null;
    }

    public void message(String str, Object... o) {
        for (UUID uuid : getPlayers()) {
            final Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                Chat.sendMessage(player, "&a&lTEAM! &7" + str, o);
            }
        }
    }
}
