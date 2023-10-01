package me.hapyl.scavenger.task.type;

import me.hapyl.scavenger.game.Board;
import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.tasks.DieFromCause;
import me.hapyl.scavenger.translate.Translate;
import me.hapyl.spigotutils.module.util.Enums;
import org.bukkit.event.entity.EntityDamageEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DieFromCauseType extends Type<EntityDamageEvent.DamageCause> {
    public DieFromCauseType() {
        super(Translate.TASK_DIE_FROM_CAUSE);

        loadAllowedFromConfig();
    }

    @Nonnull
    @Override
    public Task<EntityDamageEvent.DamageCause> createTask(@Nonnull Board board) {
        return new DieFromCause(this, board);
    }

    @Nullable
    @Override
    public EntityDamageEvent.DamageCause readType(@Nonnull String string) {
        return Enums.byName(EntityDamageEvent.DamageCause.class, string);
    }

    @Nonnull
    @Override
    public String getPath() {
        return "die_from_cause";
    }
}
