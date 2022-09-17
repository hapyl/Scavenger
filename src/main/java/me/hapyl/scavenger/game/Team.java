package me.hapyl.scavenger.game;

import com.google.common.collect.Sets;
import me.hapyl.scavenger.Message;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public enum Team {

    RED(ChatColor.RED, Material.RED_STAINED_GLASS),
    GREEN(ChatColor.GREEN, Material.LIME_STAINED_GLASS),
    BLUE(ChatColor.BLUE, Material.BLUE_STAINED_GLASS),
    PINK(ChatColor.LIGHT_PURPLE, Material.PINK_STAINED_GLASS);

    private final ChatColor color;
    private final Material itemGui;
    private final Set<UUID> players;

    Team(ChatColor color, Material itemGui) {
        this.color = color;
        this.itemGui = itemGui;
        this.players = Sets.newHashSet();
    }

    public Material getMaterial() {
        return itemGui;
    }

    public static boolean isAtLeastOneTeam() {
        for (Team value : values()) {
            if (!value.getUUIDs().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static void forEachTeam(Consumer<Team> action) {
        for (Team value : values()) {
            action.accept(value);
        }
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

    public Set<UUID> getUUIDs() {
        return players;
    }

    public void forEach(Consumer<Player> consumer) {
        getPlayers().forEach(consumer);
    }

    public Set<Player> getPlayers() {
        final Set<Player> players = Sets.newHashSet();
        for (UUID uuid : getUUIDs()) {
            final Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                players.add(player);
            }
        }

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
        for (UUID uuid : getUUIDs()) {
            final Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                Chat.sendMessage(player, "&3&lTEAM! &7" + str, o);
            }
        }
    }

    public void addToTeam(Player player) {
        final Team team = Team.getTeam(player);
        if (team == this) {
            return;
        }

        if (team != null) {
            team.removePlayer(player);
        }

        addPlayer(player);
    }

    public void giveKitStart(Player player) {
        final PlayerInventory inventory = player.getInventory();

        final int bonusItem = new Random().nextInt(3);

        inventory.addItem(createItem(Material.STONE_PICKAXE, "Started Pickaxe"));
        inventory.addItem(createItem(Material.STONE_SHOVEL, "Started Shovel"));
        inventory.addItem(createItem(Material.STONE_AXE, "Started Axe"));
        inventory.addItem(createItem(Material.BREAD, 16, "Bread"));

        // Change one of the tools to iron (Bonus)
        switch (bonusItem) {
            case 0 -> inventory.setItem(0, createItem(Material.IRON_PICKAXE, "Bonus Pickaxe"));
            case 1 -> inventory.setItem(1, createItem(Material.IRON_SHOVEL, "Bonus Shovel"));
            case 2 -> inventory.setItem(2, createItem(Material.IRON_AXE, "Bonus Axe"));
        }

        final ItemBuilder chest = new ItemBuilder(Material.LEATHER_CHESTPLATE);

        switch (color) {
            case RED -> inventory.setChestplate(chest.setLeatherArmorColor(Color.RED).build());
            case GREEN -> inventory.setChestplate(chest.setLeatherArmorColor(Color.GREEN).build());
            case BLUE -> inventory.setChestplate(chest.setLeatherArmorColor(Color.BLUE).build());
            case LIGHT_PURPLE -> inventory.setChestplate(chest.setLeatherArmorColor(Color.PURPLE).build());
        }
    }

    private ItemStack createItem(Material material, int amount, String name) {
        return new ItemBuilder(material).setAmount(amount).setName(name).build();
    }

    private ItemStack createItem(Material material, String name) {
        return createItem(material, 1, name);
    }

}
