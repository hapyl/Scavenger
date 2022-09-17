package me.hapyl.scavenger.utils;

public class StringConcator {

    private StringBuilder builder;

    public StringConcator() {
        this.builder = new StringBuilder();
    }

    public void add(String str, Object... replacements) {
        builder.append(str.formatted(replacements)).append("\n");
    }

    public void addEmpty() {
        add("");
    }

    public void empty() {
        this.builder = new StringBuilder();
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
