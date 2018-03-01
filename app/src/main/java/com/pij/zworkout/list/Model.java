package com.pij.zworkout.list;

import com.google.auto.value.AutoValue;

import java.util.List;

/**
 * <p>Created on 01/03/2018.</p>
 *
 * @author Pierrejean
 */
@AutoValue
public abstract class Model {
    public static Model create(boolean inProgress, List<WorkoutDescriptor> workouts) {
        return builder()
                .inProgress(inProgress)
                .workouts(workouts)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_Model.Builder();
    }

    public abstract boolean inProgress();

    public abstract List<WorkoutDescriptor> workouts();

    public abstract Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder inProgress(boolean inProgress);

        public abstract Builder workouts(List<WorkoutDescriptor> workouts);

        public abstract Model build();
    }
}
