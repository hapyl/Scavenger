package me.hapyl.scavenger.game;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.hapyl.scavenger.Message;
import me.hapyl.scavenger.task.Task;
import me.hapyl.scavenger.task.TaskCompletion;
import me.hapyl.scavenger.task.Type;
import me.hapyl.scavenger.task.tasks.*;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.chat.LazyClickEvent;
import me.hapyl.spigotutils.module.chat.LazyHoverEvent;
import me.hapyl.spigotutils.module.reflect.glow.Glowing;
import me.hapyl.spigotutils.module.util.ThreadRandom;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Board {

    private final long startedAt;
    private final long timeLimit;

    private final List<Task<?>> tasks;
    private final Map<Task<?>, TaskCompletion> taskCompleted;
    private final Map<Team, Integer> teamPoints;
    private final BingoWorld world;
    private final Map<UUID, BoardSettings> settings;

    public Board(long timeLimitInMinutes) {
        this.timeLimit = timeLimitInMinutes * 60000;
        this.tasks = Lists.newArrayList();
        this.taskCompleted = Maps.newHashMap();
        this.teamPoints = Maps.newHashMap();
        this.settings = Maps.newHashMap();

        // Generate Tasks
        generateTasks();

        // Generate World
        world = new BingoWorld();

        // Start timer after world-gen
        this.startedAt = System.currentTimeMillis();

        // Apply team glowing
        Team.forEachTeam(team -> {
            final Set<Player> players = team.getPlayers();
            for (Player player : players) {
                for (Player teammate : players) {
                    if (teammate == player) {
                        continue;
                    }

                    Glowing.glow(teammate, ChatColor.GREEN, (int) timeLimitInMinutes * 1200, player);
                }
            }
        });
    }

    public BoardSettings getSettings(Player player) {
        final UUID uuid = player.getUniqueId();
        return settings.computeIfAbsent(uuid, t -> new BoardSettings(uuid));
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

    public void generateTasks() {
        for (int i = 0; i < 25; i++) {
            final int random = ThreadRandom.nextInt(5);

            switch (random) {
                case 0 -> tasks.add(new BreedAnimal(randomAllowedElement(Type.BREED_ANIMAL), range(1, 4)));
                case 1 -> tasks.add(new CraftItem(randomAllowedElement(Type.CRAFT_ITEM), range(1, 2)));
                case 2 -> tasks.add(new DieFromCause(randomAllowedElement(Type.DIE_FROM_CAUSE)));
                case 3 -> tasks.add(new GatherItem(randomAllowedElement(Type.GATHER_ITEM), range(2, 6)));
                case 4 -> tasks.add(new SlayEntity(randomAllowedElement(Type.KILL_ENTITY), range(1, 5)));
            }
        }
    }

    private <T> T randomAllowedElement(Type<T> type) {
        final T random = type.randomAllowed();
        for (Task<?> task : tasks) {
            if (task.getT().equals(random)) {
                return randomAllowedElement(type);
            }
        }
        return random;
    }

    private int range(int min, int max) {
        return ThreadRandom.nextInt(min, max);
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
                EternaPlugin.getPlugin().getGlowingManager().removeGlowing(player);
            });
        });

        final String worldName = this.world.getWorldName();
        Message.broadcast("The game has ended! You were playing on %s world.", worldName);
        Bukkit.getOnlinePlayers().stream().filter(Player::isOp).forEach(player -> {
            Chat.sendClickableHoverableMessage(
                    player,
                    LazyClickEvent.RUN_COMMAND.of("/s deleteworld " + worldName),
                    LazyHoverEvent.SHOW_TEXT.of("&eClick to &c&llPERMANENTLY DELETE &e'%s'!", worldName),
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
}
