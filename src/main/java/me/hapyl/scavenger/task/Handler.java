package me.hapyl.scavenger.task;

import me.hapyl.scavenger.Board;
import me.hapyl.scavenger.Main;
import me.hapyl.scavenger.Team;
import me.hapyl.scavenger.task.tasks.DieFromCause;
import me.hapyl.scavenger.task.tasks.SlayEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

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
            completion.addCompletion(Team.getTeam(player), player);
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
        if (isDeath) {
            final Board board = Main.getManager().getBoard();
            if (board == null) {
                return;
            }

            for (Task<?> task : board.getTasks()) {
                if (task.getType() != Type.DIE_FROM_CAUSE) {
                    continue;
                }
                final DieFromCause dieTask = (DieFromCause) task;
                if (dieTask.getT() == cause) {
                    final TaskCompletion completion = board.getTaskCompletion(task);
                    completion.addCompletion(Team.getTeam(player), player);
                }
            }
        }
    }


}
