package me.hapyl.scavenger.task;

import me.hapyl.scavenger.Board;
import me.hapyl.scavenger.Main;
import me.hapyl.scavenger.Team;
import me.hapyl.scavenger.task.tasks.BreedAnimal;
import me.hapyl.scavenger.task.tasks.SlayEntity;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Task<T> {

    private final T t;
    private final Type<T> type;
    private final int amount;

    protected Task(Type<T> type, T t, int amount) {
        this.type = type;
        this.t = t;
        this.amount = amount;
    }

    public T getT() {
        return t;
    }

    public int getAmount() {
        return amount;
    }

    public Type<T> getType() {
        return type;
    }

    public ItemStack buildItem(Player player) {
        final Board board = Main.getManager().getBoard();
        if (board == null) {
            return new ItemBuilder(Material.BEDROCK)
                    .setName("&4&lError!")
                    .addSmartLore("Could not a game, report this!", "&c")
                    .build();
        }

        final ItemBuilder builder = new ItemBuilder(Material.STONE);
        final TaskCompletion completion = board.getTaskCompletion(this);
        final Team playerTeam = Team.getTeam(player);

        if (playerTeam == null) {
            return new ItemBuilder(Material.BEDROCK)
                    .setName("&4&lError!")
                    .addSmartLore("Could not find player team, report this!", "&c")
                    .build();
        }

        builder.setName(getName());

        if (this instanceof BreedAnimal breed) {
            builder.setType(Material.WHEAT);
            builder.addSmartLore("You must breed animals to complete this task.", "&8");
            builder.addLore();
            builder.addLore("&7Animals to breed &b&l" + Chat.capitalize(breed.getT()));
            builder.addLore("&7Times to breed &b&l" + amount);
        }
        else if (this instanceof SlayEntity slay) {
            builder.setType(Material.IRON_SWORD);
            builder.addSmartLore("You must kill a mob to complete this task.", "&8");
            builder.addLore();
            builder.addLore("&7Mob to kill &c&l" + Chat.capitalize(slay.getT()));
            builder.addLore("&7Times to kill &c&l" + amount);
        }

        builder.addLore();
        builder.addLore("&7Your team progress: %s/%s", playerTeam.getName(), amount);
        builder.addLore();
        builder.addLore("Points for completion:");

        final int nextPointsAward = completion.getNextPointsAward();
        switch (nextPointsAward) {
            case 10 -> builder.addLore(" &e&l10");
            case 7 -> {
                final Team team = completion.getFirstTeam();
                if (team != null) {
                    builder.addLore("&8&l&m10&7 (%s)", team.getName());
                }
                builder.addLore(" &e&l7");
            }
            case 5 -> {
                final Team team = completion.getFirstTeam();
                final Team secondTeam = completion.getSecondTeam();
                if (team != null && secondTeam != null) {
                    builder.addLore("&8&l&m10&7 (%s)", team.getName());
                    builder.addLore("&8&l&m7&7 (%s)", secondTeam.getName());
                }
                builder.addLore(" &e&l5");
                builder.addLore(" &8&oMinimum reward reached.");
            }
        }

        return builder.build();
    }

    public String getName() {
        return Chat.capitalize(t.toString()) + " " + type.getName();
    }
}
