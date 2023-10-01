package me.hapyl.scavenger.task.type;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface Types {

    /**
     * All available types.
     */
    Type<Material> CRAFT_ITEM = new CraftItemType();
    Type<Material> GATHER_ITEM = new GatherItemType();
    Type<EntityType> KILL_ENTITY = new KillEntityType();
    Type<EntityType> BREED_ANIMAL = new BreedAnimalType();
    Type<EntityDamageEvent.DamageCause> DIE_FROM_CAUSE = new DieFromCauseType();
    Type<EntityType> DIE_FROM_ENTITY = new DieFromEntityType();
    Type<Material> MINE_BLOCK = new MineBlockType();

    /**
     * Static enum-like values field.
     */
    Type<?>[] values = new Type[] {
            CRAFT_ITEM,
            GATHER_ITEM,
            KILL_ENTITY,
            BREED_ANIMAL,
            DIE_FROM_CAUSE,
            DIE_FROM_ENTITY,
            MINE_BLOCK
    };

    /**
     * Static enum-like values method.
     *
     * @return values;
     */
    @Nonnull
    static Type<?>[] values() {
        return values;
    }

    /**
     * Static enum-like byName method that retrieves an element by its name.
     *
     * @param name - Name.
     * @return A type with that name or null.
     */
    @Nullable
    static Type<?> byName(@Nonnull String name) {
        for (Type<?> value : values()) {
            if (value.getPath().equalsIgnoreCase(name)) {
                return value;
            }
        }

        return null;
    }

    /**
     * Returns list of all available type names.
     *
     * @return List of all available type names.
     */
    @Nonnull
    static List<String> names() {
        final List<String> names = Lists.newArrayList();

        for (Type<?> value : values()) {
            names.add(value.getPath());
        }

        return names;
    }
}
