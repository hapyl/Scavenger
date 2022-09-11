package me.hapyl.scavenger.task.tasks;

import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.Type;
import org.bukkit.event.entity.EntityDamageEvent;

public class DieFromCause extends Task<EntityDamageEvent.DamageCause> {

    public DieFromCause(EntityDamageEvent.DamageCause cause) {
        super(Type.DIE_FROM_CAUSE, cause, 1);
    }

    private String getDeathName() {
        final EntityDamageEvent.DamageCause cause = getT();

        return switch (cause) {
            case CONTACT -> "a Cactus, Dripstone or Berry Bush";
            case ENTITY_ATTACK -> "any entity (Melee attack)";
            case PROJECTILE -> "any projectile";
            case SUFFOCATION -> "suffocation";
            case FALL -> "falling";
            case FIRE, FIRE_TICK -> "fire";
            case LAVA -> "lava";
            case DROWNING -> "drowning";
            case BLOCK_EXPLOSION -> "TNT";
            case ENTITY_EXPLOSION -> "a Creeper";
            case LIGHTNING -> "lightning";
            case STARVATION -> "starving";
            case POISON -> "poison";
            case MAGIC -> "magic (Witch)";
            case FALLING_BLOCK -> "a falling block.";
            case HOT_FLOOR -> "standing on a magma block";
            case FREEZE -> "freezing";
            case SONIC_BOOM -> "Warden's Sonic Boom";
            default -> "anything";
        };
    }
}
