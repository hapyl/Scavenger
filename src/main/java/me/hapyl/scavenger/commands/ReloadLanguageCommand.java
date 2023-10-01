package me.hapyl.scavenger.commands;

import me.hapyl.scavenger.Main;
import me.hapyl.scavenger.translate.Translation;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import org.bukkit.entity.Player;

public class ReloadLanguageCommand extends SimplePlayerAdminCommand {
    public ReloadLanguageCommand(String command) {
        super(command);
    }

    @Override
    protected void execute(Player player, String[] strings) {
        final Translation translation = Main.getPlugin().getTranslate();
        try {
            translation.loadTranslations();
        } catch (Exception e) {
            Chat.sendMessage(player, "&cError reloading!");
            e.printStackTrace();
            return;
        }
        Chat.sendMessage(player, "&aReloaded!");
    }

}
