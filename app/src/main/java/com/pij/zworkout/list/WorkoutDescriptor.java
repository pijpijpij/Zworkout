package com.pij.zworkout.list;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

/**
 * A dummy item representing a piece of content.
 */
@AutoValue
public abstract class WorkoutDescriptor {

    public static Builder builder() {
        return new AutoValue_WorkoutDescriptor.Builder();
    }

    public static WorkoutDescriptor create(@NonNull String id, @NonNull String name, @NonNull String details) {
        return builder()
                .id(id)
                .name(name)
                .details(details)
                .build();
    }

    public abstract String id();

    public abstract String name();

    public abstract String details();

    @SuppressWarnings("NullableProblems")
    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(@NonNull String id);

        public abstract Builder name(@NonNull String name);

        public abstract Builder details(@NonNull String details);

        public abstract WorkoutDescriptor build();
    }
}
