package me.hapyl.scavenger.game;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import me.hapyl.scavenger.Message;
import me.hapyl.scavenger.task.*;
import me.hapyl.scavenger.task.type.Type;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.chat.LazyClickEvent;
import me.hapyl.spigotutils.module.chat.LazyHoverEvent;
import me.hapyl.spigotutils.module.player.PlayerLib;
import me.hapyl.spigotutils.module.reflect.glow.Glowing;
import me.hapyl.spigotutils.module.util.BukkitUtils;
import me.hapyl.spigotutils.module.util.ThreadRandom;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static me.hapyl.scavenger.translate.Translate.*;

public class Board {

    private final long timeLimit;
    private final Manager manager;
    private final Map<Task<?>, TaskData> board;
    private final Map<UUID, PlayerData> playerData;
    private final Map<Team, Integer> teamPoints;
    private final List<Type<?>> types;
    private final BingoWorld world;
    private long startedAt;

    public Board(Manager manager, long timeLimitInMinutes) {
        this.manager = manager;
        this.timeLimit = timeLimitInMinutes * 60000;
        this.board = Maps.newLinkedHashMap();
        this.playerData = Maps.newHashMap();
        this.types = Lists.newArrayList();
        this.teamPoints = Maps.newHashMap();

        // Generate Tasks
        generateTasks();

        // Generate World
        world = new BingoWorld();

        // Apply team glowing
        Team.forEachTeam(team -> {
            final Set<Player> players = team.getPlayers();
            for (Player player : players) {
                for (Player teammate : players) {
                    if (teammate == player) {
                        continue;
                    }

                    Glowing.glow(teammate, team.getColor(), (int) timeLimitInMinutes * 1200, player);
                }
            }
        });

        // Schedule starting
        new BukkitRunnable() {
            private int startTime = 200;

            @Override
            public void run() {
                if (startTime-- < 0) {
                    startedAt = System.currentTimeMillis();
                    this.cancel();

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        Chat.sendTitle(player, GAME_STARTED.get(player), "", 10, 60, 10);
                    }

                    PlayerLib.playSound(Sound.ENTITY_WITHER_SKELETON_DEATH, 0.75f);
                    PlayerLib.playSound(Sound.ENTITY_BLAZE_HURT, 0.25f);
                    PlayerLib.playSound(Sound.ENTITY_PLAYER_LEVELUP, 1.25f);

                    return;
                }

                for (Player player : Bukkit.getOnlinePlayers()) {
                    Chat.sendTitle(
                            player,
                            GAME_START_COUNTDOWN.get(player),
                            "&b%ss".formatted(BukkitUtils.roundTick(startTime)),
                            0,
                            60,
                            0
                    );
                }
            }
        }.runTaskTimer(manager.getPlugin(), 0L, 1L);
    }

    @Nonnull
    public BoardSettings getSettings(Player player) {
        final PlayerData data = getPlayerData(player);

        return data.getSettings();
    }

    public BingoWorld getWorld() {
        return world;
    }

    public int getPoints(Team team) {
        return teamPoints.computeIfAbsent(team, t -> 0);
    }

    public void addPoints(Team team, int points) {
        teamPoints.compute(team, (a, b) -> b == null ? 0 : b + points);
    }

    public boolean isStarted() {
        return startedAt != 0L;
    }

    @Nonnull
    public List<Task<?>> getTasks() {
        return Lists.newArrayList(board.keySet());
    }

    public List<Team> getTopTeams(int limit) {
        return teamPoints
                .entrySet()
                .stream()
                .sorted(Map.Entry.<Team, Integer>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey).toList();
    }

    public <T> boolean findTaskAndAdvance(@Nonnull Type<T> type, @Nonnull T t, @Nonnull Player player) {
        return findTaskAndAdvance(type, t, player, 1);
    }

    public <T> boolean findTaskAndAdvance(@Nonnull Type<T> type, @Nonnull T t, @Nonnull Player player, @Nonnull int amount) {
        final Task<?> task = findTask(type, t);
        final Team team = Team.getTeam(player);

        if (task == null || team == null) {
            return false;
        }

        final TaskCompletion completion = getTaskData(task).getTaskCompletion();

        if (completion.isComplete(player)) {
            return false;
        }

        completion.addCompletion(player, amount);
        return true;
    }

    @Nonnull
    public TaskData getTaskData(@Nonnull Task<?> task) {
        final TaskData taskData = board.get(task);

        if (taskData == null) {
            throw new NullPointerException("Data for task %s does not exist!".formatted(task.getName()));
        }

        return taskData;
    }

    @Nullable
    public <T> Task<?> findTask(@Nonnull Type<T> type, @Nonnull T t) {
        for (Task<?> task : board.keySet()) {
            if (task.compare(type, t)) {
                return task;
            }
        }

        return null;
    }

    @Nonnull
    public List<TaskCompletion> getTaskInProgress(Team team) {
        final List<TaskCompletion> list = Lists.newArrayList();

        board.forEach((task, data) -> {
            final TaskCompletion taskCompletion = data.getTaskCompletion();
            final int amount = taskCompletion.getCompletion(team);

            if (amount > 0 && amount < task.getAmount()) {
                list.add(taskCompletion);
            }
        });

        return list;
    }

    public void generateTasks() {
        for (int i = 0; i < 25; i++) {
            final Type<?> type = manager.getWeight().get();

            if (type == null) {
                continue;
            }

            final Task<?> task = type.createTask(this);
            board.put(task, new TaskData(task));

            if (!types.contains(type)) {
                types.add(type);
            }
        }
    }

    /**
     * Gets a random item that is allowed and is not yet present in the board.
     *
     * @param type - Type of the item.
     */
    public <T> T getRandomAllowedOf(@Nonnull Type<T> type) {
        final T random = type.randomAllowed();

        for (Task<?> task : board.keySet()) {
            if (task.getItem().equals(random)) {
                return getRandomAllowedOf(type);
            }
        }

        return random;
    }

    public int getRandomRange(int min, int max) {
        return ThreadRandom.nextInt(min, max + 1);
    }

    public long getStartedAt() {
        return startedAt;
    }

    public long getTimeLimit() {
        return timeLimit;
    }

    public void stop() {
        // Evacuate players
        final World world = this.world.getWorld();
        final World mainWorld = Bukkit.getWorlds().get(0);

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getWorld() == world) {
                player.teleport(mainWorld.getSpawnLocation());
            }
        }

        Bukkit.unloadWorld(world, true);

        // Stop glowing
        Team.forEachTeam(team -> {
            team.getPlayers().forEach(player -> {
            });
        });

        final String worldName = this.world.getWorldName();
        Message.broadcast("The game has ended! You were playing on %s world.", worldName);
        Bukkit.getOnlinePlayers().stream().filter(Player::isOp).forEach(player -> {
            Chat.sendClickableHoverableMessage(
                    player,
                    LazyClickEvent.RUN_COMMAND.of("/s deleteworld " + worldName),
                    LazyHoverEvent.SHOW_TEXT.of("&eClick to &c&lPERMANENTLY DELETE &e'%s'!", worldName),
                    "&6&lSCAVENGER! &7If you wish, you may &c&lPERMANENTLY DELETE &7the world. &e(Click)"
            );
        });

        broadcastWinners();
    }

    public void broadcastWinners() {
        final List<Team> topTeams = getTopTeams(3);

        for (Player player : Bukkit.getOnlinePlayers()) {
            Chat.sendCenterMessage(player, "&e&l&mxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
            Chat.sendCenterMessage(player, GAME_ENDED.get(player));
            Chat.sendCenterMessage(player, "");
            Chat.sendCenterMessage(player, GAME_ENDED_TOP_TEAMS.get(player, topTeams.size()));
            if (topTeams.isEmpty()) {
                Chat.sendCenterMessage(player, "&8None?");
            }
            else {
                topTeams.forEach(team -> {
                    final boolean isTeam = team.isPlayer(player);
                    Chat.sendCenterMessage(
                            player,
                            "%s &e- &6&l%s points%s",
                            team.getNameCaps(),
                            getPoints(team),
                            isTeam ? (" " + GAME_ENDED_YOUR_TEAM.get(player)) : ""
                    );
                });
            }

            Chat.sendCenterMessage(player, "&e&l&mxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        }

    }

    public int getTasksCompleted(UUID uuid) {
        int times = 0;

        for (TaskData value : board.values()) {
            final TaskCompletion completion = value.getTaskCompletion();
            for (Player player : completion.getPlayersCompleted()) {
                if (player.getUniqueId() == uuid) {
                    times++;
                    break;
                }
            }
        }

        return times;
    }

    public long getTimeLeft() {
        return timeLimit - (System.currentTimeMillis() - startedAt);
    }

    public String getTimeLeftString() {
        return new SimpleDateFormat("mm:ss").format(getTimeLeft());
    }

    public int getTaskTypeCount(@Nonnull Type<?> type) {
        int count = 0;

        for (Task<?> task : board.keySet()) {
            if (task.compare(type)) {
                count++;
            }
        }

        return count;
    }

    @Nonnull
    public List<Type<?>> getTypes() {
        return types;
    }

    @Nonnull
    public List<Task<?>> getPinnedTasks(Player player) {
        final PlayerData data = getPlayerData(player);

        return data.getPinnedTasks();
    }

    @Nonnull
    public PlayerData getPlayerData(Player player) {
        return playerData.computeIfAbsent(player.getUniqueId(), fn -> new PlayerData(player.getUniqueId(), this));
    }

    public boolean isPinned(Player player, Task<?> task) {
        return getPinnedTasks(player).contains(task);
    }

    public void addPinned(Player player, Task<?> task) {
        getPinnedTasks(player).add(task);
    }

    public void removePinned(Player player, Task<?> task) {
        getPinnedTasks(player).remove(task);
    }

    @Nonnull
    public TaskCompletion getTaskCompletion(Task<?> task) {
        return getTaskData(task).getTaskCompletion();
    }
}
