package com.pij.zworkout.list;

import com.google.auto.value.AutoValue;

/**
 * A dummy item representing a piece of content.
 */
@AutoValue
public abstract class WorkoutDescriptor {
    public static Builder builder() {
        return new AutoValue_WorkoutDescriptor.Builder();
    }

    public abstract String id();

    public abstract String name();

    public abstract String details();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(String id);

        public abstract Builder name(String name);

        public abstract Builder details(String details);

        public abstract WorkoutDescriptor build();
    }
}
