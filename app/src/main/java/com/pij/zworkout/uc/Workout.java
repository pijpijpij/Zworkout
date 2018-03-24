package com.pij.zworkout.uc;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

/**
 * <p>Created on 12/03/2018.</p>
 *
 * @author Pierrejean
 */
@AutoValue
public abstract class Workout {

    public static final Workout EMPTY = builder()
            .name("")
            .description("")
            .build();

    public static Builder builder() {
        return new AutoValue_Workout.Builder();
    }

    @NonNull
    public abstract String name();

    @NonNull
    public abstract String description();

    public abstract Builder toBuilder();

    public Workout name(String name) {
        return toBuilder().name(name).build();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder name(String name);

        public abstract Builder description(String description);

        public abstract Workout build();
    }
}
