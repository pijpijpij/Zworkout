package com.pij.zworkout.workout;

import com.annimon.stream.Optional;
import com.google.auto.value.AutoValue;

import org.jetbrains.annotations.NotNull;

/**
 * <p>Created on 01/03/2018.</p>
 *
 * @author Pierrejean
 */
@AutoValue
public abstract class Model {

    public static Builder builder() {
        return new AutoValue_Model.Builder();
    }

    public static Model create(boolean inProgress, Optional<String> showError, boolean showSaved, String name, boolean nameIsReadOnly) {
        return builder()
                .inProgress(inProgress)
                .showError(showError)
                .showSaved(showSaved)
                .name(name)
                .nameIsReadOnly(nameIsReadOnly)
                .build();
    }

    public abstract boolean inProgress();

    @NotNull
    public abstract Optional<String> showError();

    public abstract boolean showSaved();

    @NotNull
    public abstract String name();

    public abstract boolean nameIsReadOnly();

    public abstract Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder inProgress(boolean inProgress);

        public abstract Builder showError(Optional<String> showError);

        public abstract Builder showSaved(boolean showSaved);

        public abstract Builder name(String name);

        public abstract Builder nameIsReadOnly(boolean nameIsReadOnly);

        public abstract Model build();
    }
}
