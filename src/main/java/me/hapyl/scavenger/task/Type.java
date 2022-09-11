package me.hapyl.scavenger.task;

import com.google.common.collect.Sets;
import me.hapyl.spigotutils.module.util.CollectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Breedable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.List;
import java.util.Set;

import static org.bukkit.event.entity.EntityDamageEvent.DamageCause.values;

public class Type<T> {

    public static final Type<Material> CRAFT_ITEM = new Type<>("Crafter");
    public static final Type<Material> GATHER_ITEM = new Type<>("Gatherer");
    public static final Type<EntityType> KILL_ENTITY = new Type<>("Slayer");
    public static final Type<EntityType> BREED_ANIMAL = new Type<>("Farmer");
    public static final Type<EntityDamageEvent.DamageCause> DIE_FROM_CAUSE = new Type<>("Walking Dead");

    static {
        // Material related
        for (Material material : Material.values()) {
            // Gather item
            if (material.isItem()) {
                GATHER_ITEM.addAllowed(material);
            }

            // Craftable
            final List<Recipe> recipe = Bukkit.getRecipesFor(new ItemStack(material));
            if (!recipe.isEmpty()) {
                CRAFT_ITEM.addAllowed(material);
            }
        }

        // Entity related
        for (EntityType value : EntityType.values()) {
            if (value.isAlive()) {
                KILL_ENTITY.addAllowed(value);
            }

            final Class<? extends Entity> entityClass = value.getEntityClass();
            if (entityClass != null && Breedable.class.isAssignableFrom(entityClass)) {
                switch (value) {
                    case SKELETON_HORSE, ZOMBIE_HORSE, MUSHROOM_COW, OCELOT, POLAR_BEAR, TRADER_LLAMA, HOGLIN, STRIDER, WANDERING_TRADER, VILLAGER -> {
                    }
                    default -> BREED_ANIMAL.addAllowed(value);
                }
            }
        }

        // Death from cause
        for (EntityDamageEvent.DamageCause cause : values()) {
            switch (cause) {
                case ENTITY_SWEEP_ATTACK, MELTING, VOID, SUICIDE, DRAGON_BREATH, CUSTOM, FLY_INTO_WALL, DRYOUT -> {
                }
                default -> DIE_FROM_CAUSE.addAllowed(cause);
            }
        }

    }

    private final String name;
    private final Set<T> allowed;

    Type(String name) {
        this.name = name;
        this.allowed = Sets.newHashSet();
    }

    public void addAllowed(T... t) {
        allowed.addAll(List.of(t));
    }

    public boolean isAllowed(T t) {
        return allowed.contains(t);
    }

    public Set<T> getAllowed() {
        return allowed;
    }

    public T randomAllowed() {
        return CollectionUtils.randomElement(allowed);
    }

    public String getName() {
        return name;
    }
}
