package com.pij.zworkout.service.api;

import android.support.annotation.NonNull;

import com.annimon.stream.Optional;
import com.google.auto.value.AutoValue;

import java.net.URI;

/**
 * <p>Created on 02/03/2018.</p>
 *
 * @author Pierrejean
 */
@AutoValue
public abstract class WorkoutFile {

    public static WorkoutFile UNDEFINED = WorkoutFile.create(URI.create("empty"), "", Optional.empty());

    public static WorkoutFile create(URI uri, String name, Optional<String> detail) {
        return builder()
                .uri(uri)
                .name(name)
                .detail(detail)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_WorkoutFile.Builder();
    }

    @NonNull
    public abstract URI uri();

    @NonNull
    public abstract String name();

    @NonNull
    public abstract Optional<String> detail();

    public abstract Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder uri(URI uri);

        public abstract Builder name(String name);

        public abstract Builder detail(Optional<String> detail);

        public abstract WorkoutFile build();
    }
}
