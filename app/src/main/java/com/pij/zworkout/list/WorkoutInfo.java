package com.pij.zworkout.list;

import com.annimon.stream.Optional;
import com.google.auto.value.AutoValue;

/**
 * A dummy item representing a piece of content.
 */
@AutoValue
public abstract class WorkoutInfo {

    public static Builder builder() {
        return new AutoValue_WorkoutInfo.Builder();
    }

    public static WorkoutInfo create(String id, String name, Optional<String> details) {
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

        public abstract WorkoutInfo build();
    }
}
