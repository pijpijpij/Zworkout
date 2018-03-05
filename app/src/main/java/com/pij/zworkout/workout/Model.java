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

    public static Model create(boolean inProgress, Optional<String> showError, String name) {
        return builder()
                .inProgress(inProgress)
                .showError(showError)
                .name(name)
                .build();
    }

    public abstract boolean inProgress();

    @NotNull
    public abstract Optional<String> showError();

    @NotNull
    public abstract String name();

    public abstract Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder inProgress(boolean inProgress);

        public abstract Builder showError(Optional<String> showError);

        public abstract Builder name(String name);

        public abstract Model build();
    }
}