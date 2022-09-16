package me.hapyl.scavenger.game;

import me.hapyl.scavenger.Message;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.player.EffectType;
import me.hapyl.spigotutils.module.player.PlayerLib;
import me.hapyl.spigotutils.module.util.ThreadRandom;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class BingoWorld {

    private final WorldCreator creator;
    private final String hexName;
    private final World world;

    public BingoWorld() {
        hexName = generateWorldName();
        creator = new WorldCreator("bingo_" + hexName);

        Chat.broadcastOp("&c[ADMIN] &7Generating bingo world '%s'...", creator.name());
        Chat.broadcastOp("&c[ADMIN] &7The server WILL lag during generation!");
        Chat.sendTitles("&a&lGENERATING WORLD", "&7Please wait...", 0, 20000, 0);

        world = creator.createWorld();
        if (world == null) {
            Chat.broadcastOp("&c[ADMIN] &eError generating world!");
            return;
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

        Message.sendStartTitle(player);
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
