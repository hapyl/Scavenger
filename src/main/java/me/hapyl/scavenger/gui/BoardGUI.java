package me.hapyl.scavenger.gui;

import me.hapyl.scavenger.Main;
import me.hapyl.scavenger.game.Board;
import me.hapyl.scavenger.game.BoardSettings;
import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.TaskCompletion;
import me.hapyl.scavenger.task.Type;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.inventory.gui.PlayerGUI;
import me.hapyl.spigotutils.module.player.PlayerLib;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BoardGUI extends PlayerGUI {

    private static final ItemStack ICON_PANE = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("&f").build();

    private final Board board;

    public BoardGUI(Player player) {
        super(player, "Scavenger Board", 5);
        board = Main.getPlugin().getManager().getBoard();
        updateInventory();
    }

    public void updateInventory() {
        PlayerLib.playSound(getPlayer(), Sound.ITEM_BOOK_PAGE_TURN, 0.0f);

        if (board == null) {
            setItem(
                    22,
                    new ItemBuilder(Material.REDSTONE_BLOCK)
                            .setName("&cNo Board!")
                            .setSmartLore("It looks like there is no board yet, ask admin to start the game!")
                            .build()
            );
        }
        else {
            final List<Task<?>> tasks = board.getTasks();

            fillColumn(0, ICON_PANE);
            fillColumn(1, ICON_PANE);
            fillColumn(7, ICON_PANE);
            fillColumn(8, ICON_PANE);

            final Player player = getPlayer();
            final BoardSettings settings = board.getSettings(player);
            final boolean renderPaneEnabled = settings.isEnabled(BoardSettings.Setting.RENDER_COMPLETED_GREEN);

            // Simple Render button
            setItem(
                    17,
                    new ItemBuilder(renderPaneEnabled ? Material.LIME_DYE : Material.GRAY_DYE)
                            .setName("Simple Completed Tasks")
                            .addSmartLore("If enabled, completed tasks will be rendered as simple Lime Glass Pane.")
                            .addLore()
                            .addLore("&eClick to %s", renderPaneEnabled ? "disable" : "enable")
                            .build(),
                    pt -> {
                        settings.setEnabled(BoardSettings.Setting.RENDER_COMPLETED_GREEN, !renderPaneEnabled);
                        PlayerLib.playSound(player, Sound.BLOCK_LEVER_CLICK, 1.0f);
                        updateInventory();
                    }
            );

            // Sort button
            setItem(
                    35,
                    new ItemBuilder(Material.NAME_TAG)
                            .setName("Sort")
                            .addSmartLore("Sort tasks by the type for easier navigation.")
                            .addLore()
                            .addLore("&7Current Sort: &b" + settings.getTypeRenderName())
                            .addLore("")
                            .addLore("&eClick to switch")
                            .build(), pt -> {
                        settings.nextTypeRender();
                        PlayerLib.playSound(player, Sound.BLOCK_LEVER_CLICK, 1.0f);
                        updateInventory();
                    }
            );

            int slot = 2;
            for (Task<?> task : tasks) {
                final TaskCompletion completion = board.getTaskCompletion(task);
                if (completion.isComplete(player) && renderPaneEnabled) {
                    setItem(
                            slot,
                            new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName(task.getName()).addLore("&a&lCOMPLETED!").build()
                    );
                }
                else {
                    final Type<?> renderType = settings.getTypeRender();
                    if (renderType == null || renderType == task.getType()) {
                        setItem(slot, task.buildItem(player));
                    }
                    else {
                        setItem(
                                slot,
                                new ItemBuilder(Material.BARRIER)
                                        .setName("&aIncompatible Type!")
                                        .setSmartLore("This task is incompatible with your sort settings!")
                                        .build()
                        );
                    }
                }

                // Keep last
                slot += slot % 9 == 6 ? 5 : 1;
            }
        }

        openInventory();
    }
}
