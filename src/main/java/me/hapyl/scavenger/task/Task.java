package me.hapyl.scavenger.task;

import me.hapyl.scavenger.Main;
import me.hapyl.scavenger.game.Board;
import me.hapyl.scavenger.game.Team;
import me.hapyl.scavenger.task.tasks.BreedAnimal;
import me.hapyl.scavenger.task.tasks.TranslateTask;
import me.hapyl.scavenger.task.type.Type;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public abstract class Task<T> implements TranslateTask {

    private final Board board;
    private final T t;
    private final Type<T> type;
    private final int amount;

    public Task(@Nonnull Type<T> type, @Nonnull Board board) {
        this(type, board, type.getCountMin(), type.getCountMax());
    }

    public Task(@Nonnull Type<T> type, @Nonnull Board board, int countMin, int countMax) {
        this.type = type;
        this.t = board.getRandomAllowedOf(type);
        this.amount = countMin == countMax ? countMin : board.getRandomRange(countMin, countMax);
        this.board = board;
    }

    @Nonnull
    public T getItem() {
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

    public ItemStack buildItem(Player player) {
        final Board board = Main.getPlugin().getManager().getBoard();
        if (board == null) {
            return new ItemBuilder(Material.BEDROCK).setName("&4&lError!").addSmartLore("Could not a game! Report this!", "&c").build();
        }

        final ItemBuilder builder = new ItemBuilder(this.getMaterial()).setAmount(amount);
        final TaskCompletion completion = board.getTaskCompletion(this);
        final Team playerTeam = Team.getTeam(player);

        if (playerTeam == null) {
            return new ItemBuilder(Material.BEDROCK)
                    .setName("&4&lError!")
                    .addSmartLore("Could not find player's team, report this!", "&c")
                    .build();
        }

        builder.setName(getName());
        translateLore(builder, player, this);
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

        builder.addLore();
        if (board.isPinned(player, this)) {
            builder.addLore("&d&lPINNED!");
            builder.addLore("&eClick to unpin!");
        }
        else {
            builder.addLore("&eClick to pin!");
        }

        return builder.hideFlags().build();
    }

    public String getName() {
        return Chat.capitalize(t.toString()) + " &l" + type.getName();
    }

    public <C> boolean compare(Type<C> type) {
        if (type == null) {
            return false;
        }

        return this.type.equals(type);
    }

    public <C> boolean compare(C c) {
        if (c == null) {
            return false;
        }

        return this.t.equals(c);
    }

    public <C> boolean compare(Type<C> type, C t) {
        return compare(type) && compare(t);
    }

    protected void translateLore(ItemBuilder builder, Player player, TranslateTask translateTask) {
        builder.addSmartLore(translateTask.getDescription().get(player), "&8&o");
        builder.addLore();
        builder.addLore(translateTask.getTypeDescription().get(player, translateTask.formatItem()));
        builder.addLore(translateTask.getAmountDescription().get(player, getAmount()));
    }
}
