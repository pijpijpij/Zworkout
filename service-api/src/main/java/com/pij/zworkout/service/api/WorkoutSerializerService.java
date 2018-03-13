package com.pij.zworkout.service.api;

import java.io.File;

import io.reactivex.Completable;

/**
 * <p>Created on 12/03/2018.</p>
 *
 * @author Pierrejean
 */

public interface WorkoutSerializerService {
    Completable write(Workout data, File target);
}
