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

    public static Model create(boolean inProgress, Optional<String> showError) {
        return builder()
                .inProgress(inProgress)
                .showError(showError)
                .build();
    }

    public abstract boolean inProgress();

    @NotNull
    public abstract Optional<String> showError();

    public abstract Builder toBuilder();

    @SuppressWarnings("NullableProblems")
    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder inProgress(boolean inProgress);

        public abstract Builder showError(Optional<String> showError);

        public abstract Model build();
    }
}
