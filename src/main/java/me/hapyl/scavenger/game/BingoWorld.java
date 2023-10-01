package me.hapyl.scavenger.game;

import me.hapyl.scavenger.utils.ScavengerItem;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.player.EffectType;
import me.hapyl.spigotutils.module.player.PlayerLib;
import me.hapyl.spigotutils.module.util.Nulls;
import me.hapyl.spigotutils.module.util.ThreadRandom;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import static me.hapyl.scavenger.translate.Translate.GAME_GENERATING_WORLD_SUBTITLE;
import static me.hapyl.scavenger.translate.Translate.GAME_GENERATING_WORLD_TITLE;

public class BingoWorld {

    private final WorldCreator creator;
    private final String hexName;
    private World world;

    public BingoWorld() {
        hexName = generateWorldName();
        creator = new WorldCreator("bingo_" + hexName);

        Chat.broadcastOp("&c[ADMIN] &7Generating bingo world '%s'...", creator.name());
        Chat.broadcastOp("&c[ADMIN] &7The server WILL lag during generation!");

        for (Player player : Bukkit.getOnlinePlayers()) {
            Chat.sendTitle(player, GAME_GENERATING_WORLD_TITLE.get(player), GAME_GENERATING_WORLD_SUBTITLE.get(player), 0, 20000, 0);
        }

        try {
            world = creator.createWorld();

            if (world == null) {
                Chat.broadcastOp("&c[ADMIN] &eError generating world!");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // World generation finished
        Chat.clearTitles();
        Chat.broadcastOp("&c[ADMIN] &aFinished generating bingo world '%s'!", creator.name());

        prepareWorld();

        Team.forEachTeam(team -> {
            for (Player player : team.getPlayers()) {
                preparePlayer(player);
            }
        });
    }

    private void prepareWorld() {
        if (world == null) {
            return;
        }

        world.setGameRule(GameRule.KEEP_INVENTORY, true);

        final WorldBorder worldBorder = world.getWorldBorder();
        worldBorder.setCenter(world.getSpawnLocation());
        worldBorder.setSize(1000);
    }

    private void preparePlayer(Player player) {
        if (world == null) {
            Chat.sendMessage(player, "&cWorld is null!");
            return;
        }

        player.getInventory().clear();
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        player.setSaturation(20.0f);
        player.setFoodLevel(20);
        player.setLevel(0);
        player.setExp(0.0f);
        player.setGameMode(GameMode.SURVIVAL);
        player.setMaxHealth(20.0f);
        player.setHealth(player.getMaxHealth());

        PlayerLib.addEffect(player, EffectType.BLINDNESS, 60, 1);

        final Location location = world.getSpawnLocation();

        player.setBedSpawnLocation(location, true);
        player.teleport(location);

        Nulls.runIfNotNull(Team.getTeam(player), team -> team.giveKitStart(player));

        // Give board item
        ScavengerItem.ITEM_BOARD_SHORTCUT.put(player, 8);
    }

    public String getHexName() {
        return hexName;
    }

    public String getWorldName() {
        return "bingo_" + hexName;
    }

    public World getWorld() {
        return world;
    }

    private String generateWorldName() {
        final StringBuilder builder = new StringBuilder();
        final String chars = "abcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < 6; i++) {
            builder.append(chars.toCharArray()[ThreadRandom.nextInt(chars.length())]);
        }

        return builder.toString();
    }
}
