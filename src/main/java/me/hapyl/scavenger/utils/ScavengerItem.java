package me.hapyl.scavenger.utils;

import me.hapyl.scavenger.gui.BoardGUI;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;
import java.util.function.Consumer;

public enum ScavengerItem {

    ITEM_BOARD_SHORTCUT(Material.DAYLIGHT_DETECTOR, "&aBoard", "Click to open the scavenger menu!", BoardGUI::new);

    private final ItemStack item;
    private final String name;
    private final String about;

    ScavengerItem(Material material, String name, String about, Consumer<Player> action) {
        this.name = name;
        this.about = about;
        this.item = new ItemBuilder(material, this.name().toLowerCase(Locale.ROOT))
                .setName(name + " &7(Click)")
                .setSmartLore(about)
                .addClickEvent(action).build();
    }

    public String getName() {
        return name;
    }

    public ItemStack getItem() {
        return item;
    }

    public String getAbout() {
        return about;
    }

    public void give(Player player) {
        player.getInventory().addItem(item);
    }

    public void put(Player player, int slot) {
        player.getInventory().setItem(slot, item);
    }

    public void remove(Player player) {
        player.getInventory().remove(item);
    }

    public boolean has(Player player) {
        return player.getInventory().contains(item);
    }

    public boolean compare(ItemStack item) {
        return item.equals(this.item);
    }
}
