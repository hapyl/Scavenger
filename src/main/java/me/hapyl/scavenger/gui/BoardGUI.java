package me.hapyl.scavenger.gui;

import me.hapyl.scavenger.Main;
import me.hapyl.scavenger.game.Board;
import me.hapyl.scavenger.game.BoardSettings;
import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.TaskCompletion;
import me.hapyl.scavenger.task.type.Type;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.inventory.gui.PlayerGUI;
import me.hapyl.spigotutils.module.player.PlayerLib;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static me.hapyl.scavenger.translate.Translate.*;

public class BoardGUI extends PlayerGUI {

    private static final ItemStack ICON_PANE = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("&f").build();

    private final Board board;

    public BoardGUI(Player player) {
        super(player, BOARD_NAME.get(player), 5);
        board = Main.getPlugin().getManager().getBoard();
        updateInventory();
        openInventory();
        PlayerLib.playSound(getPlayer(), Sound.ITEM_BOOK_PAGE_TURN, 0.0f);
    }

    public void updateInventory() {
        final Player player = getPlayer();
        if (board == null) {
            setItem(
                    22,
                    new ItemBuilder(Material.REDSTONE_BLOCK)
                            .setName(BOARD_NO_BOARD.get(player))
                            .setSmartLore(BOARD_NO_BOARD1.get(player))
                            .build()
            );
        }
        else {
            fillColumns();

            final List<Task<?>> tasks = board.getTasks();
            final BoardSettings settings = board.getSettings(player);
            final Type<?> currentRender = settings.getTypeFilter();
            final boolean renderPaneEnabled = settings.isEnabled(BoardSettings.Setting.RENDER_COMPLETED_GREEN);
            final boolean pinnedEnabled = settings.isEnabled(BoardSettings.Setting.SHOW_PINNED);

            // Simple Render button
            setItem(
                    17,
                    new ItemBuilder(renderPaneEnabled ? Material.LIME_DYE : Material.GRAY_DYE)
                            .setName(BOARD_SETTING_SIMPLE_TASKS.get(player))
                            .addSmartLore(BOARD_SETTING_SIMPLE_TASKS1.get(player))
                            .addLore()
                            .predicate(renderPaneEnabled, ItemBuilder::glow)
                            .addLore(renderPaneEnabled ? BOARD_CLICK_TO_DISABLE.get(player) : BOARD_CLICK_TO_ENABLE.get(player))
                            .build(),
                    pt -> {
                        settings.setEnabled(BoardSettings.Setting.RENDER_COMPLETED_GREEN, !renderPaneEnabled);
                        playClickSoundAndUpdateInventory();
                    }
            );

            final ItemBuilder sortItem = new ItemBuilder(Material.NAME_TAG)
                    .setName(BOARD_SETTING_SORT.get(player))
                    .addSmartLore(BOARD_SETTING_SORT1.get(player))
                    .addLore();

            sortItem.addLore(currentRender == null ?
                    " &b→ &b&l" + BOARD_SETTING_FILTER_NONE.get(player) : "&b" + BOARD_SETTING_FILTER_NONE.get(player));

            board.getTypes().forEach(type -> {
                sortItem.addLore(
                        "%s%s &7(%s)",
                        type == currentRender ? " &b→ &b&l" : "&b",
                        type.getName(player),
                        board.getTaskTypeCount(type)
                );
            });

            // Sort button
            setItem(
                    35,
                    sortItem
                            .addLore("")
                            .addLore(BOARD_CYCLE_FORWARD.get(player))
                            .addLore(BOARD_CYCLE_BACKWARDS.get(player))
                            .predicate(currentRender != null, ItemBuilder::glow)
                            .build(),
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
            setItem(
                    26,
                    new ItemBuilder(Material.REDSTONE_TORCH)
                            .setName(BOARD_SETTING_SHOW_PINNED.get(player))
                            .setSmartLore(BOARD_SETTING_SHOW_PINNED1.get(player))
                            .addLore()
                            .predicate(pinnedEnabled, ItemBuilder::glow)
                            .addLore(pinnedEnabled ? BOARD_CLICK_TO_DISABLE.get(player) : BOARD_CLICK_TO_ENABLE.get(player))
                            .build(),
                    pl -> {
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
                        setItem(slot, new ItemBuilder(Material.COAL).setName(BOARD_SETTING_NOT_PINNED.get(player)).build());
                    }
                    else {
                        setItem(slot, task.buildItem(player), pl -> {
                            final List<Task<?>> pinned = board.getPinnedTasks(pl);

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
                                    .setName(BOARD_SETTING_FILTER_WRONG_TYPE.get(player))
                                    .setSmartLore(BOARD_SETTING_FILTER_WRONG_TYPE1.get(player))
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
