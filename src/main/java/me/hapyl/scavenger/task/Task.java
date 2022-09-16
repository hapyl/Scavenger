package me.hapyl.scavenger.task;

import me.hapyl.scavenger.Main;
import me.hapyl.scavenger.game.Board;
import me.hapyl.scavenger.game.Team;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Task<T> {

    private final T t;
    private final Type<T> type;
    private final int amount;

    public Task(Type<T> type, T t, int amount) {
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

    public ItemStack getMaterial() {
        return new ItemStack(Material.STONE);
    }

    public void appendLore(ItemBuilder builder) {
        builder.addSmartLore("&c&lSomeone forgot to add lore for this task, oh well, you will never know what to do ¯\\(ツ)_/¯");
    }

    public ItemStack buildItem(Player player) {
        final Board board = Main.getPlugin().getManager().getBoard();
        if (board == null) {
            return new ItemBuilder(Material.BEDROCK).setName("&4&lError!").addSmartLore("Could not a game, report this!", "&c").build();
        }

        final ItemBuilder builder = new ItemBuilder(this.getMaterial()).setAmount(amount);
        final TaskCompletion completion = board.getTaskCompletion(this);
        final Team playerTeam = Team.getTeam(player);

        if (playerTeam == null) {
            return new ItemBuilder(Material.BEDROCK)
                    .setName("&4&lError!")
                    .addSmartLore("Could not find player team, report this!", "&c")
                    .build();
        }

        builder.setName(getName());
        appendLore(builder);
        builder.addLore();

        if (completion.isComplete(playerTeam)) {
            builder.glow();
            builder.addLore("&7Your team progress: &a&lCOMPLETED!");
        }
        else {
            builder.addLore("&7Your team progress: &e%s/%s", completion.getCompletion(playerTeam), amount);
        }

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

        return builder.hideFlags().build();
    }

    public String getName() {
        return Chat.capitalize(t.toString()) + " &l" + type.getName();
    }
}
