package com.pij.zworkout.list;

import android.support.annotation.NonNull;

import com.annimon.stream.Optional;
import com.google.auto.value.AutoValue;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * <p>Created on 01/03/2018.</p>
 *
 * @author Pierrejean
 */
// TODO add error string
@AutoValue
public abstract class Model {

    public static Builder builder() {
        return new AutoValue_Model.Builder();
    }

    public static Model create(boolean inProgress, Optional<String> showError, Optional<WorkoutInfo> showWorkout, boolean createWorkout, List<WorkoutInfo> workouts) {
        return builder()
                .inProgress(inProgress)
                .showError(showError)
                .showWorkout(showWorkout)
                .createWorkout(createWorkout)
                .workouts(workouts)
                .build();
    }

    public abstract boolean inProgress();

    @NotNull
    public abstract Optional<String> showError();

    @NonNull
    public abstract Optional<WorkoutInfo> showWorkout();

    public abstract boolean createWorkout();

    @NonNull
    public abstract List<WorkoutInfo> workouts();

    public abstract Builder toBuilder();

    @SuppressWarnings("NullableProblems")
    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder inProgress(boolean inProgress);

        public abstract Builder showError(Optional<String> showError);

        public abstract Builder workouts(@NonNull List<WorkoutInfo> workouts);

        public abstract Builder showWorkout(@NonNull Optional<WorkoutInfo> showWorkout);

        public abstract Builder createWorkout(boolean createWorkout);

        public abstract Model build();
    }
}
