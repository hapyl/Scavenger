package me.hapyl.scavenger.task;

import me.hapyl.scavenger.InjectListener;
import me.hapyl.scavenger.Main;
import me.hapyl.scavenger.game.Board;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

public class Handler extends InjectListener {

    public Handler(Main main) {
        super(main);
    }

    @EventHandler()
    public void handleEntityDeathEvent(EntityDeathEvent ev) {
        final LivingEntity entity = ev.getEntity();
        final Player player = entity.getKiller();

        final Board board = getPlugin().getManager().getBoard();
        if (board == null || player == null) {
            return;
        }

        board.findTaskAndAdvance(Type.KILL_ENTITY, entity.getType(), player, 1);
    }

    @EventHandler()
    public void handleCraftItem(CraftItemEvent ev) {
        final HumanEntity human = ev.getWhoClicked();
        final ItemStack item = ev.getCurrentItem();

        final Board board = getPlugin().getManager().getBoard();
        if (item == null || board == null || !(human instanceof Player player) || ev.getInventory().getType() == InventoryType.CRAFTING) {
            return;
        }

        final Task<?> task = board.findTask(Type.CRAFT_ITEM, item.getType());
        if (task == null) {
            return;
        }

        final TaskCompletion completion = board.getTaskCompletion(task);

        if (completion.isComplete(player)) {
            return;
        }

        final int canGive = calculateCanGive(task, player, item);

        ev.setCancelled(true);
        final CraftingInventory craft = ev.getInventory();
        final ItemStack[] matrix = craft.getMatrix();
        for (ItemStack stack : matrix) {
            if (stack == null) {
                continue;
            }
            stack.setAmount(stack.getAmount() - 1);
        }

        craft.setMatrix(matrix);
        completion.addCompletion(player, canGive);
    }

    @EventHandler()
    public void handleItemPickup(EntityPickupItemEvent ev) {
        final LivingEntity entity = ev.getEntity();
        final Board board = getPlugin().getManager().getBoard();
        final ItemStack item = ev.getItem().getItemStack();

        if (!(entity instanceof Player player) || board == null) {
            return;
        }

        final Task<?> task = board.findTask(Type.GATHER_ITEM, item.getType());
        if (task == null) {
            return;
        }

        final TaskCompletion completion = board.getTaskCompletion(task);
        if (completion.isComplete(player)) {
            return;
        }

        final int canGive = calculateCanGive(task, player, item);

        ev.getItem().setItemStack(item);
        completion.addCompletion(player, canGive);
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
        final Board board = getPlugin().getManager().getBoard();
        if (!isDeath || board == null) {
            return;
        }

        if (ev instanceof EntityDamageByEntityEvent evEntity) {
            final Entity damager = evEntity.getDamager();
            final Task<?> task = board.findTask(Type.DIE_FROM_ENTITY, damager.getType());
            if (task != null) {
                board.getTaskCompletion(task).addCompletion(player, 1);
                return;
            }
        }

        board.findTaskAndAdvance(Type.DIE_FROM_CAUSE, cause, player, 1);
    }

    @EventHandler()
    public void handleAdvancement(PlayerAdvancementDoneEvent ev) {
        final Player player = ev.getPlayer();
        final Advancement advancement = ev.getAdvancement();

        final Board board = getPlugin().getManager().getBoard();
        if (board == null) {
            return;
        }

        board.findTaskAndAdvance(Type.ADVANCEMENT_ADVANCER, advancement, player, 1);
    }

    @EventHandler()
    public void handleEntityDamageByEntity(EntityDamageByEntityEvent ev) {
        final Entity entity = ev.getEntity();
        final Entity damager = ev.getDamager();

        if (!(entity instanceof Player player)) {
            return;
        }

        final boolean isDeath = ev.getFinalDamage() >= player.getHealth();
        final Board board = getPlugin().getManager().getBoard();
        if (!isDeath || board == null) {
            return;
        }

        board.findTaskAndAdvance(Type.DIE_FROM_ENTITY, damager.getType(), player, 1);
    }

    @EventHandler()
    public void handleBreedAnimals(EntityBreedEvent ev) {
        final LivingEntity entity = ev.getEntity();
        final LivingEntity breeder = ev.getBreeder();

        if (!(breeder instanceof Player player)) {
            return;
        }

        final Board board = getPlugin().getManager().getBoard();
        if (board == null) {
            return;
        }

        board.findTaskAndAdvance(Type.BREED_ANIMAL, entity.getType(), player, 1);
    }

    private int calculateCanGive(Task<?> task, Player player, ItemStack item) {
        final Board board = getPlugin().getManager().getBoard();
        if (board == null) {
            return 0;
        }

        final TaskCompletion completion = board.getTaskCompletion(task);
        final int needMore = task.getAmount() - completion.getCompletion(player);

        int canGive = 0;
        for (int i = 0; i < needMore; i++) {
            if (item.getAmount() <= 0) {
                break;
            }

            canGive++;
            item.setAmount(item.getAmount() - 1);
        }

        return canGive;
    }


}
