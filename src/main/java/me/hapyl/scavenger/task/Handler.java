package me.hapyl.scavenger.task;

import me.hapyl.scavenger.InjectListener;
import me.hapyl.scavenger.Main;
import me.hapyl.scavenger.game.Board;
import me.hapyl.scavenger.task.type.Types;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryType;
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

        board.findTaskAndAdvance(Types.KILL_ENTITY, entity.getType(), player);
    }

    @EventHandler()
    public void handleCraftItem(CraftItemEvent ev) {
        final HumanEntity human = ev.getWhoClicked();
        final ItemStack item = ev.getCurrentItem();

        final Board board = getPlugin().getManager().getBoard();
        if (item == null || board == null || !(human instanceof Player player) || ev.getInventory().getType() == InventoryType.CRAFTING) {
            return;
        }

        final Task<?> task = board.findTask(Types.CRAFT_ITEM, item.getType());
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

        final Task<?> task = board.findTask(Types.GATHER_ITEM, item.getType());
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
            if (!(damager instanceof LivingEntity living)) {
                return;
            }

            if (living instanceof Projectile projectile && projectile.getShooter() instanceof LivingEntity livingShooter) {
                living = livingShooter;
            }

            board.findTaskAndAdvance(Types.DIE_FROM_ENTITY, living.getType(), player);
            System.out.println("Called");
        }

        board.findTaskAndAdvance(Types.DIE_FROM_CAUSE, cause, player);
        System.out.println("Called cause/");
    }

    @EventHandler()
    public void handleBlockBreak(BlockBreakEvent ev) {
        final Player player = ev.getPlayer();
        final Block block = ev.getBlock();

        final Board board = getPlugin().getManager().getBoard();
        if (board == null) {
            return;
        }

        if (!board.findTaskAndAdvance(Types.MINE_BLOCK, block.getType(), player)) {
            return;
        }

        ev.setDropItems(false);
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

        board.findTaskAndAdvance(Types.DIE_FROM_ENTITY, damager.getType(), player);
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

        board.findTaskAndAdvance(Types.BREED_ANIMAL, entity.getType(), player);
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
