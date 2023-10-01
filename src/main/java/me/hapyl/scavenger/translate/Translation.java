package me.hapyl.scavenger.translate;

import com.google.common.collect.Maps;
import me.hapyl.scavenger.Inject;
import me.hapyl.scavenger.Main;
import me.hapyl.spigotutils.module.nbt.NBT;
import me.hapyl.spigotutils.module.util.CollectionUtils;
import me.hapyl.spigotutils.module.util.Wrap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Translation extends Inject {

    private final Map<String, Language> byName;
    private final Map<UUID, Language> playerLanguage;

    public Translation(Main main) {
        super(main);
        this.byName = Maps.newHashMap();
        this.playerLanguage = Maps.newHashMap();

        loadTranslations();
    }

    @Nullable
    public Language getPlayerLang(Player player) {
        return playerLanguage.get(player.getUniqueId());
    }

    public void loadPlayerLangFromNbt(Player player) {
        final String lang = NBT.getString(player, "scavenger_lang", null);
        if (lang == null) {
            return;
        }

        setPlayerLang(player, byName(lang));
    }

    @Nullable
    public Language byName(String name) {
        return byName.get(name);
    }

    public void setPlayerLang(Player player, Language language) {
        playerLanguage.put(player.getUniqueId(), language);
        NBT.setString(player, "scavenger_lang", language.getName());
    }

    public void loadTranslations() {
        final File langFolder = new File(Main.getPlugin().getDataFolder() + "/lang");
        final File[] files = langFolder.listFiles();
        if (files == null) {
            return;
        }

        int loaded = 0;
        for (File file : files) {
            if (!file.getName().endsWith(".yml")) {
                continue;
            }

            final String langName = file.getName().replace(".yml", "").toLowerCase();
            byName.put(langName, initDefaultFields(new Language(langName)));
            loaded++;
        }

        Bukkit.getLogger().info("Loaded %s translation files:".formatted(loaded));
        Bukkit.getLogger().info(CollectionUtils.wrapToString(byName.keySet(), Wrap.DEFAULT));
    }

    public Set<String> getLanguages() {
        return byName.keySet();
    }

    private Language initDefaultFields(Language language) {
        for (Translate st : Translate.values()) {
            final String path = st.path();
            if (language.getConfig().get(path) == null) {
                language.setString(st, st.getDefault());
            }
        }

        language.reloadConfig();
        return language;
    }

}
