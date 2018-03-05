package com.pij.zworkout.workout;

import com.annimon.stream.Optional;
import com.google.auto.value.AutoValue;

/**
 * A dummy item representing a piece of content.
 */
@AutoValue
public abstract class WorkoutDescriptor {

    public static Builder builder() {
        return new AutoValue_WorkoutDescriptor.Builder();
    }

    public static WorkoutDescriptor create(String id, String name, Optional<String> details) {
        return builder()
                .id(id)
                .name(name)
                .details(details)
                .build();
    }

    public abstract String id();

    public abstract String name();

    public abstract Optional<String> details();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(String id);

        public abstract Builder name(String name);

        public abstract Builder details(Optional<String> details);

        public abstract WorkoutDescriptor build();
    }
}
