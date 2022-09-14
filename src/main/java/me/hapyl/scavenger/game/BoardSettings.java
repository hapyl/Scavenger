package me.hapyl.scavenger.game;

import com.google.common.collect.Sets;
import me.hapyl.scavenger.task.Type;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BoardSettings {

    private final UUID uuid;
    private final Set<Setting> enabled;
    private Type<?> typeRender;


    public BoardSettings(UUID uuid) {
        this.uuid = uuid;
        this.enabled = Sets.newHashSet();
        this.typeRender = null;
    }

    public Type<?> getTypeRender() {
        return typeRender;
    }

    public String getTypeRenderName() {
        return typeRender == null ? "All" : typeRender.getName();
    }

    public void nextTypeRender() {
        final List<Type<?>> values = Type.values;

        if (typeRender == null) {
            typeRender = values.get(0);
            return;
        }

        for (int i = 0; i < values.size(); i++) {
            final Type<?> type = values.get(i);
            if (type != typeRender) {
                continue;
            }

            typeRender = (values.size() > i + 1 ? values.get(i + 1) : null);
            break;
        }
    }

    public void setEnabled(Setting setting, boolean flag) {
        if (flag) {
            enabled.add(setting);
        }
        else {
            enabled.remove(setting);
        }
    }

    public boolean isEnabled(Setting setting) {
        return enabled.contains(setting);
    }

    public boolean isDisabled(Setting setting) {
        return !isEnabled(setting);
    }

    public enum Setting {
        RENDER_COMPLETED_GREEN
    }

}
