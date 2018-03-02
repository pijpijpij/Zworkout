package com.pij.zworkout.list;

import android.support.annotation.NonNull;

import com.annimon.stream.Optional;
import com.google.auto.value.AutoValue;

import java.util.List;

/**
 * <p>Created on 01/03/2018.</p>
 *
 * @author Pierrejean
 */
// TODO add error string
@AutoValue
public abstract class Model {

    public static Model create(boolean inProgress,
                               @NonNull Optional<WorkoutDescriptor> showWorkout,
                               @NonNull List<WorkoutDescriptor> workouts) {
        return builder()
                .inProgress(inProgress)
                .showWorkout(showWorkout)
                .workouts(workouts)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_Model.Builder();
    }

    public abstract boolean inProgress();

    @NonNull
    public abstract Optional<WorkoutDescriptor> showWorkout();

    @NonNull
    public abstract List<WorkoutDescriptor> workouts();

    public abstract Builder toBuilder();

    @SuppressWarnings("NullableProblems")
    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder inProgress(boolean inProgress);

        public abstract Builder workouts(@NonNull List<WorkoutDescriptor> workouts);

        public abstract Builder showWorkout(@NonNull Optional<WorkoutDescriptor> showWorkout);

        public abstract Model build();
    }
}
