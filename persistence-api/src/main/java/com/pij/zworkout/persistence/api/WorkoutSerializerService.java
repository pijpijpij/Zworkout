package com.pij.zworkout.persistence.api;

import android.support.annotation.NonNull;

import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * <p>Created on 12/03/2018.</p>
 *
 * @author Pierrejean
 */

public interface WorkoutSerializerService {

    @NonNull
    Completable write(@NonNull PersistableWorkout data, @NonNull OutputStream target);

    @NonNull
    Single<PersistableWorkout> read(@NonNull InputStream source);
}
