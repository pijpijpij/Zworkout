package com.pij.zworkout.workout;

import com.annimon.stream.Optional;
import com.google.auto.value.AutoValue;
import com.pij.zworkout.uc.Workout;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * <p>Created on 01/03/2018.</p>
 *
 * @author Pierrejean
 */
@AutoValue
public abstract class State {

    public static Builder builder() {
        return new AutoValue_State.Builder();
    }

    public static State create(boolean inProgress, Optional<String> showError, boolean showSaved, Workout workout, boolean nameIsEditable, Optional<File> file) {
        return builder()
                .inProgress(inProgress)
                .showError(showError)
                .showSaved(showSaved)
                .workout(workout)
                .nameIsEditable(nameIsEditable)
                .file(file)
                .build();
    }

    public abstract boolean inProgress();

    @NotNull
    public abstract Optional<String> showError();

    public abstract boolean showSaved();

    @NotNull
    public abstract Workout workout();

    public abstract boolean nameIsEditable();

    @NotNull
    public abstract Optional<File> file();

    public abstract Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder inProgress(boolean inProgress);

        public abstract Builder showError(Optional<String> showError);

        public abstract Builder showSaved(boolean showSaved);

        public abstract Builder workout(Workout workout);

        public abstract Builder file(Optional<File> file);

        public abstract Builder nameIsEditable(boolean nameIsEditable);

        public abstract State build();
    }

}
