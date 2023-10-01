package me.hapyl.scavenger.translate;

import me.hapyl.scavenger.Main;
import me.hapyl.spigotutils.module.util.BFormat;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public enum Translate {

    SET_LANG("&aChanged language to &e{lang}&a."),
    SET_LANG_ABOUT("&eTranslations are not 100% correct and not all elements are translated."),

    // UI (Scoreboard, Tablist)
    UI_WAITING_FOR_GAME("&eWaiting for game to begin..."),
    UI_NOT_IN_TEAM("&cNot in a team!"),
    UI_YOUR_TEAM("&f&lYour Team:"),
    UI_IN_PROGRESS_TASKS("&f&lIn Progress Tasks:"),
    UI_NONE("&8None!"),
    UI_MORE_ELEMENTS("&8And {amount} more!"),
    UI_PINNED_TASKS("&f&lPinned Tasks:"),
    UI_PINNED_ABOUT0("&7Pin tasks by clicking on them."),
    UI_PINNED_ABOUT1("&7Pinned tasks are NOT shared."),
    UI_TIME_LEFT("&fTime Left: &e{time}"),
    UI_TOP_TEAMS("&fTop Teams:"),
    UI_COMMAND_INFO("&e/scavenger &fto see board"),

    //
    GAME_GENERATING_WORLD_TITLE("&a&lGENERATING WORLD"),
    GAME_GENERATING_WORLD_SUBTITLE("&7Please wait..."),
    GAME_STARTED("&a&lTHE GAME BEGINS!"),
    GAME_START_COUNTDOWN("&a&lSTARTING IN"),

    //
    GAME_ENDED("&6&lSCAVENGER ENDED!"),
    GAME_ENDED_TOP_TEAMS("&7Top {teams} Teams:"),
    GAME_ENDED_YOUR_TEAM("&a(Your Team)"),

    CHAT_TEAM_PREFIX("&3&l(Team)"),

    ITEM_STARTER_PICKAXE("Starter Pickaxe"),
    ITEM_STARTER_SHOVEL("Starter Shovel"),
    ITEM_STARTER_AXE("Starter Axe"),
    ITEM_STARTER_BREAD("Bread"),
    ITEM_BONUS_PICKAXE("Bonus Pickaxe"),
    ITEM_BONUS_SHOVEL("Bonus Shovel"),
    ITEM_BONUS_AXE("Bonus Axe"),

    BOARD_NAME("Scavenger Board"),
    BOARD_NO_BOARD("&cNo Board!"),
    BOARD_NO_BOARD1("It looks like there is no board yet, ask an admin to start the game!"),

    BOARD_SETTING_SIMPLE_TASKS("Simple Completed Tasks"),
    BOARD_SETTING_SIMPLE_TASKS1("If enabled, completed tasks will be rendered as simple Lime Glass Pane."),
    BOARD_CLICK_TO_ENABLE("&eClick to enable"),
    BOARD_CLICK_TO_DISABLE("&eClick to disable"),

    BOARD_SETTING_SORT("Sort"),
    BOARD_SETTING_SORT1("Sort tasks by the type for easier navigation."),

    BOARD_SETTING_FILTER_NONE("No Filter"),
    BOARD_SETTING_FILTER_WRONG_TYPE("&cIncompatible Type!"),
    BOARD_SETTING_FILTER_WRONG_TYPE1("This task is incompatible with your sort settings!"),
    BOARD_CYCLE_FORWARD("&eLeft click to cycle forward"),
    BOARD_CYCLE_BACKWARDS("&eRight click to cycle backwards"),

    BOARD_SETTING_SHOW_PINNED("&aShow Pinned"),
    BOARD_SETTING_SHOW_PINNED1("If enabled, only your pinned tasks will be shown. Pinned tasks are NOT shared."),
    BOARD_SETTING_NOT_PINNED("&cNot Pinned!"),

    // Type names,
    TASK_CRAFT_ITEM("Crafter"),
    TASK_GATHER_ITEM("Gatherer"),
    TASK_KILL_ENTITY("Slayer"),
    TASK_BREED_ANIMAL("Farmer"),
    TASK_DIE_FROM_CAUSE("Death"),
    TASK_DIE_FROM_ENTITY("Victim"),
    TASK_MINE_BLOCK("Miner"),

    // Tasks,
    TASK_BREED_ANIMALS_ABOUT("Your team must breed animals to advance this task."),
    TASK_BREED_ANIMALS_TYPE("&7Animals to breed &b&l{type}"),
    TASK_BREED_ANIMALS_AMOUNT("&7Times to breed &b&l{amount}"),

    TASK_CRAFT_ITEM_ABOUT("Your team must craft an item to advance this task."),
    TASK_CRAFT_ITEM_TYPE("&7Item to craft &e&l{type}"),
    TASK_CRAFT_ITEM_AMOUNT("&7Times to craft &e&l{amount}"),

    TASK_MINE_BLOCK_ABOUT("Your team must mine a block to advance this task."),
    TASK_MINE_BLOCK_TYPE("&7Block to mine &3&l{type}"),
    TASK_MINE_BLOCK_AMOUNT("&7Times to mine &3&l{amount}"),

    TASK_DIE_FROM_CAUSE_ABOUT("Your team must die from a certain cause to advance this task."),
    TASK_DIE_FROM_CAUSE_TYPE("&7Die from &4&l{type}"),
    TASK_DIE_FROM_CAUSE_AMOUNT("&7Times to die &4&l{amount}"),

    TASK_DIE_FROM_ENTITY_ABOUT("Your team must die from a certain mob to advance this task."),
    TASK_DIE_FROM_ENTITY_TYPE("&7Die from &c&l{type}"),
    TASK_DIE_FROM_ENTITY_AMOUNT("&7Times to die &c&l{amount}"),

    TASK_GATHER_ITEM_ABOUT("Your team must gather (pick up) an item to advance this task."),
    TASK_GATHER_ITEM_TYPE("&7Item to pickup &6&l{type}"),
    TASK_GATHER_ITEM_AMOUNT("&7Amount to pickup &6&l{amount}"),

    TASK_KILL_ENTITY_ABOUT("Your team must kill a mob to advance this task."),
    TASK_KILL_ENTITY_TYPE("&7Entity to kill &c&l{type}"),
    TASK_KILL_ENTITY_AMOUNT("&7Times to kill &c&l{amount}"),

    ;

    private final String def;

    Translate(String def) {
        this.def = def;
    }

    @Nonnull
    public String get(Player player, Object... format) {
        return get(Main.getPlugin().getTranslate().getPlayerLang(player), format);
    }

    @Nonnull
    public String get(@Nullable Language language, Object... format) {
        final String string = get(language);

        return BFormat.format(string, format);
    }

    @Nonnull
    public String get(@Nullable Language language) {
        return language == null ? def : language.getString(path(), def);
    }

    final String path() {
        return name().toLowerCase();
    }

    public String getDefault() {
        return def;
    }
}
