package me.hapyl.scavenger.commands;

import me.hapyl.scavenger.Main;
import me.hapyl.scavenger.translate.Language;
import me.hapyl.scavenger.translate.Translate;
import me.hapyl.scavenger.translate.Translation;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.SimplePlayerCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class LanguageCommand extends SimplePlayerCommand {
    public LanguageCommand(String name) {
        super(name);
        setAliases("lang");
        setUsage("lang (Language)");
    }

    @Override
    protected void execute(Player player, String[] args) {
        // lang (Lang)
        if (args.length != 1) {
            sendInvalidUsageMessage(player);
            return;
        }

        final Translation translation = Main.getPlugin().getTranslate();
        final Language language = translation.byName(args[0].toLowerCase());

        if (language == null) {
            Chat.sendMessage(player, "&cInvalid language.");
            return;
        }

        if (language == translation.getPlayerLang(player)) {
            Chat.sendMessage(player, "&cAlready using %s language.", language.getName());
            return;
        }

        translation.setPlayerLang(player, language);
        Chat.sendMessage(player, Translate.SET_LANG.get(language, language.getName()));
        Chat.sendMessage(player, Translate.SET_LANG_ABOUT.get(language));
    }

    @Override
    protected List<String> tabComplete(CommandSender sender, String[] args) {
        return completerSort(Main.getPlugin().getTranslate().getLanguages(), args);
    }
}
