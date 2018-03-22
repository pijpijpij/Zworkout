package com.pij.zworkout.persistence.xml;

import com.pij.zworkout.persistence.api.PersistableWorkout;
import com.pij.zworkout.persistence.api.WorkoutSerializerService;

import java.io.OutputStream;

import io.reactivex.Completable;

/**
 * <p>Created on 13/03/2018.</p>
 *
 * @author Pierrejean
 */
class DummyWorkoutSerializerService implements WorkoutSerializerService {

    @Override
    public Completable write(PersistableWorkout data, OutputStream target) {
        return Completable.error(new UnsupportedOperationException("write([data, target]) not implemented."));
    }

}
