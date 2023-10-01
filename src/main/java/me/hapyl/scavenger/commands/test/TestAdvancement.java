package me.hapyl.scavenger.commands.test;

import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;

public class TestAdvancement extends SimplePlayerAdminCommand {
    public TestAdvancement(String name) {
        super(name);
    }

    @Override
    protected void execute(Player player, String[] strings) {
        final Advancement advancement = Bukkit.advancementIterator().next();

        Chat.sendMessage(player, "Fetching %s...", advancement);
    }
}
