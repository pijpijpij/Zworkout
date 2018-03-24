package com.pij.zworkout.service.api;

import android.support.annotation.NonNull;

import com.annimon.stream.Optional;
import com.google.auto.value.AutoValue;

import java.net.URI;

import static com.annimon.stream.Optional.empty;

/**
 * <p>Created on 02/03/2018.</p>
 *
 * @author Pierrejean
 */
@AutoValue
public abstract class WorkoutFile {

    public static WorkoutFile UNDEFINED = WorkoutFile.builder().name("").build();

    public static WorkoutFile create(@NonNull URI uri, @NonNull String name) {
        return builder()
                .uri(Optional.of(uri))
                .name(name)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_WorkoutFile.Builder()
                .uri(empty())
                ;
    }

    public static WorkoutFile create(Optional<URI> uri, String name) {
        return builder()
                .uri(uri)
                .name(name)
                .build();
    }

    @NonNull
    public abstract Optional<URI> uri();

    @NonNull
    public abstract String name();

    public abstract Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder uri(Optional<URI> uri);

        public abstract Builder name(String name);

        public abstract WorkoutFile build();
    }
}
