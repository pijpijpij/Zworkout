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

    public static WorkoutFile create(@NonNull URI uri, @NonNull String name, @NonNull Optional<String> detail) {
        return create(Optional.of(uri), name, detail);
    }

    public static WorkoutFile create(Optional<URI> uri, String name, Optional<String> detail) {
        return builder()
                .uri(uri)
                .name(name)
                .detail(detail)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_WorkoutFile.Builder()
                .detail(empty())
                .uri(empty())
                ;
    }

    @NonNull
    public abstract Optional<URI> uri();

    @NonNull
    public abstract String name();

    @NonNull
    public abstract Optional<String> detail();

    public abstract Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder uri(Optional<URI> uri);

        public abstract Builder name(String name);

        public abstract Builder detail(Optional<String> detail);

        public abstract WorkoutFile build();
    }
}
