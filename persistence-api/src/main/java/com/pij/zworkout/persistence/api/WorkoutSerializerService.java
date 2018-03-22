package com.pij.zworkout.persistence.api;

import java.io.OutputStream;

import io.reactivex.Completable;

/**
 * <p>Created on 12/03/2018.</p>
 *
 * @author Pierrejean
 */

public interface WorkoutSerializerService {
    Completable write(PersistableWorkout data, OutputStream target);
}
