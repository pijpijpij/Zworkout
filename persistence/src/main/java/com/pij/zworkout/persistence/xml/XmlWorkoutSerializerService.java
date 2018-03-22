package com.pij.zworkout.persistence.xml;

import com.pij.zworkout.persistence.api.PersistableWorkout;
import com.pij.zworkout.persistence.api.WorkoutSerializerService;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.OutputStream;

import io.reactivex.Completable;

/**
 * <p>Created on 16/03/2018.</p>
 *
 * @author Pierrejean
 */

class XmlWorkoutSerializerService implements WorkoutSerializerService {
    private final Serializer serializer = new Persister();

    @Override
    public Completable write(PersistableWorkout data, OutputStream target) {
        return Completable.fromAction(() -> serializer.write(data, target));
    }
}
