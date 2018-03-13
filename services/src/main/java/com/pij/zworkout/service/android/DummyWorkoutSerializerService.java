package com.pij.zworkout.service.android;

import com.pij.zworkout.service.api.Workout;
import com.pij.zworkout.service.api.WorkoutSerializerService;

import java.io.File;

import io.reactivex.Completable;

/**
 * <p>Created on 13/03/2018.</p>
 *
 * @author Pierrejean
 */
class DummyWorkoutSerializerService implements WorkoutSerializerService {

    @Override
    public Completable write(Workout data, File target) {
        return Completable.error(new UnsupportedOperationException("write([data, target]) not implemented."));
    }
}
