package me.hapyl.scavenger.task;

import me.hapyl.scavenger.Main;
import me.hapyl.scavenger.game.Board;
import me.hapyl.scavenger.task.tasks.SlayEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class Handler implements Listener {

    @EventHandler()
    public void handleEntityDeathEvent(EntityDeathEvent ev) {
        final LivingEntity entity = ev.getEntity();
        final Player player = entity.getKiller();

        final Board board = Main.getManager().getBoard();
        if (board == null) {
            return;
        }

        for (Task<?> task : board.getTasks()) {
            if (task.getType() != Type.KILL_ENTITY) {
                continue;
            }

            final SlayEntity slay = (SlayEntity) task;
            if (slay.getT() != entity.getType()) {
                continue;
            }

            final TaskCompletion completion = board.getTaskCompletion(slay);
            completion.addCompletion(player, 1);
        }
    }

    @EventHandler()
    public void handleDeathEvent(EntityDamageEvent ev) {
        final Entity entity = ev.getEntity();
        final EntityDamageEvent.DamageCause cause = ev.getCause();

        if (!(entity instanceof Player player)) {
            return;
        }

        // Player death check
        final boolean isDeath = ev.getFinalDamage() >= player.getHealth();
        final Board board = Main.getManager().getBoard();
        if (!isDeath || board == null) {
            return;
        }

        final Task<?> task = board.findTask(Type.DIE_FROM_CAUSE, cause);
        if (task == null) {
            return;
        }

        final TaskCompletion completion = board.getTaskCompletion(task);
        completion.addCompletion(player, 1);
    }


    @EventHandler()
    public void handleItemPickup(EntityPickupItemEvent ev) {
        final LivingEntity entity = ev.getEntity();
        final Board board = Main.getManager().getBoard();
        final ItemStack item = ev.getItem().getItemStack();

        if (!(entity instanceof Player player) || board == null) {
            return;
        }

        final Task<?> task = board.findTask(Type.GATHER_ITEM, item.getType());
        if (task == null) {
            return;
        }

        final TaskCompletion completion = board.getTaskCompletion(task);

        final int completed = completion.getCompletion(player);
        final int totalNeeded = task.getAmount();
        final int needMore = totalNeeded - completed;

        int canGive = 0;
        for (int i = 0; i < needMore; i++) {
            if (item.getAmount() <= 0) {
                break;
            }

            canGive++;
            item.setAmount(item.getAmount() - 1);
        }

        ev.getItem().setItemStack(item);
        completion.addCompletion(player, canGive);
    }


}
