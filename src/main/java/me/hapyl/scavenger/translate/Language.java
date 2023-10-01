package me.hapyl.scavenger.translate;

import me.hapyl.scavenger.Main;
import me.hapyl.spigotutils.module.config.Config;

public class Language extends Config {

    private final String name;

    public Language(String name) {
        super(Main.getPlugin(), "/lang", name);
        this.name = name.toLowerCase();
    }

    public final void setString(Translate path, String value) {
        set(path.path(), value);
    }

    public final String getString(Translate st) {
        return st.get(this);
    }

    public String getName() {
        return name;
    }

}
