package me.hapyl.scavenger.game;

import com.google.common.collect.Sets;
import me.hapyl.scavenger.task.type.Type;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class BoardSettings {

    private final Board board;
    private final Set<Setting> enabled;
    private Type<?> typeFilter;

    public BoardSettings(@Nonnull Board board) {
        this.board = board;
        this.enabled = Sets.newHashSet();
        this.typeFilter = null;
    }

    @Nullable
    public Type<?> getTypeFilter() {
        return typeFilter;
    }

    public void nextTypeFilter() {
        final List<Type<?>> values = board.getTypes();

        if (typeFilter == null) {
            typeFilter = values.get(0);
            return;
        }

        for (int i = 0; i < values.size(); i++) {
            final Type<?> type = values.get(i);
            if (type != typeFilter) {
                continue;
            }

            typeFilter = (values.size() > i + 1 ? values.get(i + 1) : null);
            break;
        }
    }

    public void previousTypeFilter() {
        final List<Type<?>> values = board.getTypes();

        if (typeFilter == null) {
            typeFilter = values.get(values.size() - 1);
            return;
        }

        for (int i = 0; i < values.size(); i++) {
            final Type<?> type = values.get(i);

            if (type != typeFilter) {
                continue;
            }

            typeFilter = (i == 0 ? null : values.get(i - 1));
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
        RENDER_COMPLETED_GREEN {
            @Override
            public String toString() {
                return "Render Completed Tasks Green";
            }
        },
        SHOW_PINNED {
            @Override
            public String toString() {
                return "Show Pinned";
            }
        }
    }

}
