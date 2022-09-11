package me.hapyl.scavenger;

import me.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.entity.Player;

public class Message {

    public static final String PREFIX = "&6&lSCAVENGER! &7";

    public static void sendMessage(Player player, Object message, Object... replacements) {
        Chat.sendMessage(player, PREFIX + message, replacements);
    }

    public static void broadcast(Object message, Object... replacements) {
        Chat.broadcast(PREFIX + message, replacements);
    }

}
