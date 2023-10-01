package me.hapyl.scavenger.task.tasks;

import me.hapyl.scavenger.translate.Translate;

import javax.annotation.Nonnull;

public interface TranslateTask {

    @Nonnull
    String formatItem();

    @Nonnull
    Translate getDescription();

    @Nonnull
    Translate getTypeDescription();

    @Nonnull
    Translate getAmountDescription();


}
