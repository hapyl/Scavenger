package me.hapyl.scavenger.gui;

import me.hapyl.scavenger.Main;
import me.hapyl.scavenger.game.Board;
import me.hapyl.scavenger.task.Task;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.inventory.gui.PlayerGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class BoardGUI extends PlayerGUI {

    private final Board board;

    public BoardGUI(Player player) {
        super(player, "Scavenger Board", 5);
        board = Main.getManager().getBoard();
        updateInventory();
    }

    public void updateInventory() {
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

            int slot = 2;
            for (Task<?> task : tasks) {
                setItem(slot, task.buildItem(getPlayer()));
                slot += slot % 9 == 6 ? 5 : 1;
            }
        }

        openInventory();
    }
}
