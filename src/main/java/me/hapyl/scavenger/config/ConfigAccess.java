package me.hapyl.scavenger.config;

import me.hapyl.scavenger.Main;
import org.bukkit.configuration.file.FileConfiguration;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public interface ConfigAccess {

    @Nonnull
    String getPrefix();

    @Nonnull
    String getPath();

    default int getCountMin() {
        return Math.max(getConfig().getInt(getFullPath() + "min", 1), 1);
    }

    default int getCountMax() {
        return Math.min(getConfig().getInt(getFullPath() + "max", 5), 100);
    }

    @Nonnull
    default Set<String> getAllowedValues() {
        return new HashSet<>(getConfig().getStringList(getFullPath() + "allowed"));
    }

    default String getFullPath() {
        String prefix = getPrefix();

        if (!prefix.endsWith(".")) {
            prefix = prefix + ".";
        }

        final String path = getPath();

        if (path.contains(".")) {
            throw new IllegalArgumentException("Path cannot contain a dot. (.)");
        }

        return prefix + path + ".";
    }

    @Nonnull
    default FileConfiguration getConfig() {
        return Main.getPlugin().getConfig();
    }

}
