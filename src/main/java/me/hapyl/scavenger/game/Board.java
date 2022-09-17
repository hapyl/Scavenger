package me.hapyl.scavenger.game;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import me.hapyl.scavenger.Message;
import me.hapyl.scavenger.task.LinkedType;
import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.TaskCompletion;
import me.hapyl.scavenger.task.Type;
import me.hapyl.scavenger.task.tasks.*;
import me.hapyl.spigotutils.EternaPlugin;
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

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Board {

    private final long timeLimit;
    private long startedAt;

    private final Manager manager;
    private final List<Task<?>> tasks;
    private final List<Type<?>> types;
    private final Map<Task<?>, TaskCompletion> taskCompleted;
    private final Map<UUID, Set<Task<?>>> pinned;
    private final Map<Team, Integer> teamPoints;
    private final BingoWorld world;
    private final Map<UUID, BoardSettings> settings;

    public Board(Manager manager, long timeLimitInMinutes) {
        this.manager = manager;
        this.timeLimit = timeLimitInMinutes * 60000;
        this.tasks = Lists.newArrayList();
        this.types = Lists.newArrayList();
        this.pinned = Maps.newHashMap();
        this.taskCompleted = Maps.newHashMap();
        this.teamPoints = Maps.newHashMap();
        this.settings = Maps.newHashMap();

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

                    Chat.sendTitles("&a&lTHE GAME BEGINS!", "", 10, 60, 10);

                    PlayerLib.playSound(Sound.ENTITY_WITHER_SKELETON_DEATH, 0.75f);
                    PlayerLib.playSound(Sound.ENTITY_BLAZE_HURT, 0.25f);
                    PlayerLib.playSound(Sound.ENTITY_PLAYER_LEVELUP, 1.25f);

                    return;
                }

                Chat.sendTitles("&a&lSTARTING IN", "&b%ss".formatted(BukkitUtils.roundTick(startTime)), 0, 20, 0);
            }
        }.runTaskTimer(manager.getPlugin(), 0L, 1L);
    }

    public BoardSettings getSettings(Player player) {
        final UUID uuid = player.getUniqueId();
        return settings.computeIfAbsent(uuid, t -> new BoardSettings(this, uuid));
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

    public List<Task<?>> getTasks() {
        return tasks;
    }

    public List<Team> getTopTeams(int limit) {
        return teamPoints
                .entrySet()
                .stream()
                .sorted(Map.Entry.<Team, Integer>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey).toList();
    }

    public <T> void findTaskAndAdvance(Type<T> type, T t, Player player, int amount) {
        final Task<?> task = findTask(type, t);
        final Team team = Team.getTeam(player);
        if (task == null || team == null) {
            return;
        }

        getTaskCompletion(task).addCompletion(player, amount);
    }

    public <T> Task<?> findTask(Type<T> type, T t) {
        for (Task<?> task : tasks) {
            if (task.getType() == type && task.getT() == t) {
                return task;
            }
        }

        return null;
    }

    public List<TaskCompletion> getTaskInProgress(Team team) {
        final List<TaskCompletion> list = Lists.newArrayList();
        taskCompleted.forEach((task, completion) -> {
            final int amount = completion.getCompletion(team);
            if (amount > 0 && amount < task.getAmount()) {
                list.add(completion);
            }
        });
        return list;
    }

    public TaskCompletion getTaskCompletion(Task<?> task) {
        return taskCompleted.computeIfAbsent(task, TaskCompletion::new);
    }

    // Jesus this is probably the worst implementation,
    // but I can't think of anything better than this.
    public void generateTasks() {
        for (int i = 0; i < 25; i++) {
            final LinkedType type = manager.getWeight().get();

            final Type<?> link = type.getLink();
            if (!types.contains(link)) {
                types.add(link);
            }

            switch (type) {
                case CRAFT_ITEM -> tasks.add(new CraftItem(this));
                case GATHER_ITEM -> tasks.add(new GatherItem(this));
                case BREED_ANIMAL -> tasks.add(new BreedAnimal(this));
                case DIE_FROM_CAUSE -> tasks.add(new DieFromCause(this));
                case DIE_FROM_ENTITY -> tasks.add(new DieFromEntity(this));
                case KILL_ENTITY -> tasks.add(new SlayEntity(this));
            }
        }
    }

    public <T> T getRandomAllowedOf(Type<T> type) {
        final T random = type.randomAllowed();
        for (Task<?> task : tasks) {
            if (task.getT().equals(random)) {
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
                EternaPlugin.getPlugin().getGlowingManager().stopGlowing(player);
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
            Chat.sendCenterMessage(player, "&6&lSCAVENGER ENDED!");
            Chat.sendCenterMessage(player, "");
            Chat.sendCenterMessage(player, "&7Top %s Teams:", topTeams.size());
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
                            isTeam ? " &a(Your Team)" : ""
                    );
                });
            }

            Chat.sendCenterMessage(player, "&e&l&mxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        }

    }

    public int getTasksCompleted(UUID uuid) {
        int times = 0;
        for (TaskCompletion completion : taskCompleted.values()) {
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

    public int getTaskTypeAmount(Type<?> type) {
        int i = 0;
        for (Task<?> task : tasks) {
            if (task.getType() == type) {
                i++;
            }
        }
        return i;
    }

    public List<Type<?>> getTypes() {
        return types;
    }

    public Set<Task<?>> getPinnedTasks(Player player) {
        return pinned.computeIfAbsent(player.getUniqueId(), t -> Sets.newLinkedHashSet());
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
}
