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
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

public class BoardGUI extends PlayerGUI {

    private static final ItemStack ICON_PANE = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("&f").build();

    private final Board board;

    public BoardGUI(Player player) {
        super(player, "Scavenger Board", 5);
        board = Main.getPlugin().getManager().getBoard();
        updateInventory();
        openInventory();
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
            fillColumns();

            final List<Task<?>> tasks = board.getTasks();
            final Player player = getPlayer();
            final BoardSettings settings = board.getSettings(player);
            final Type<?> currentRender = settings.getTypeFilter();
            final boolean renderPaneEnabled = settings.isEnabled(BoardSettings.Setting.RENDER_COMPLETED_GREEN);
            final boolean pinnedEnabled = settings.isEnabled(BoardSettings.Setting.SHOW_PINNED);

            // Simple Render button
            setItem(
                    17,
                    new ItemBuilder(renderPaneEnabled ? Material.LIME_DYE : Material.GRAY_DYE)
                            .setName("Simple Completed Tasks")
                            .addSmartLore("If enabled, completed tasks will be rendered as simple Lime Glass Pane.")
                            .addLore()
                            .predicate(renderPaneEnabled, ItemBuilder::glow)
                            .addLore("&eClick to %s", renderPaneEnabled ? "disable" : "enable")
                            .build(),
                    pt -> {
                        settings.setEnabled(BoardSettings.Setting.RENDER_COMPLETED_GREEN, !renderPaneEnabled);
                        playClickSoundAndUpdateInventory();
                    }
            );

            final ItemBuilder sortItem = new ItemBuilder(Material.NAME_TAG)
                    .setName("Sort")
                    .addSmartLore("Sort tasks by the type for easier navigation.")
                    .addLore();

            sortItem.addLore(currentRender == null ? " &b→ &b&lNo Filter" : "&bNo Filter");

            board.getTypes().forEach(type -> {
                sortItem.addLore("%s%s &7(%s)", type == currentRender ? " &b→ &b&l" : "&b", type.getName(), board.getTaskTypeAmount(type));
            });

            // Sort button
            setItem(
                    35,
                    sortItem.addLore("")
                            .addLore("&eLeft click to cycle forward")
                            .addLore("&eRight click to cycle backwards")
                            .predicate(currentRender != null, ItemBuilder::glow).build(),
                    click -> {
                        settings.nextTypeFilter();
                        playClickSoundAndUpdateInventory();
                    },
                    ClickType.LEFT
            );

            setClick(35, click -> {
                settings.previousTypeFilter();
                playClickSoundAndUpdateInventory();
            }, ClickType.RIGHT);

            // Show pinned button
            setItem(26,
                    new ItemBuilder(Material.REDSTONE_TORCH)
                            .setName("&aShow Pinned")
                            .setSmartLore("If enabled, only your pinned tasks will be shown. Pinned tasks are NOT shared.")
                            .addLore()
                            .predicate(pinnedEnabled, ItemBuilder::glow)
                            .addLore("&eClick to %s", pinnedEnabled ? "disable" : "enable")
                            .build(), pl -> {
                        settings.setEnabled(BoardSettings.Setting.SHOW_PINNED, !pinnedEnabled);
                        playClickSoundAndUpdateInventory();
                    }
            );

            // Render Tasks
            int slot = 2;
            for (Task<?> task : tasks) {
                final boolean isPinned = board.isPinned(player, task);
                final TaskCompletion completion = board.getTaskCompletion(task);

                // Simple Render
                if (completion.isComplete(player) && renderPaneEnabled) {
                    setItem(
                            slot,
                            new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName(task.getName()).addLore("&a&lCOMPLETED!").build()
                    );
                }

                // Filter Render
                if (currentRender == null || currentRender == task.getType()) {
                    if (pinnedEnabled && !isPinned) {
                        setItem(slot, new ItemBuilder(Material.COAL).setName("&cNot Pinned!").build());
                    }
                    else {
                        setItem(slot, task.buildItem(player), pl -> {
                            final Set<Task<?>> pinned = board.getPinnedTasks(pl);

                            if (pinned.contains(task)) {
                                pinned.remove(task);
                            }
                            else {
                                pinned.add(task);
                            }

                            playClickSoundAndUpdateInventory();
                        });
                    }
                }
                else if (currentRender != task.getType()) {
                    setItem(
                            slot,
                            new ItemBuilder(Material.BARRIER)
                                    .setName("&cIncompatible Type!")
                                    .setSmartLore("This task is incompatible with your sort settings!")
                                    .build()
                    );
                }

                slot += slot % 9 == 6 ? 5 : 1;
            }
        }

    }

    private void fillColumns() {
        fillColumn(0, ICON_PANE);
        fillColumn(1, ICON_PANE);
        fillColumn(7, ICON_PANE);
        fillColumn(8, ICON_PANE);
    }

    private void playClickSoundAndUpdateInventory() {
        final Player player = getPlayer();
        PlayerLib.playSound(player, Sound.BLOCK_LEVER_CLICK, 1.0f);
        updateInventory();
        openInventory();
    }

}
