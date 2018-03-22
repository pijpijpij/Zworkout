package com.pij.zworkout.uc;

import com.pij.zworkout.persistence.api.PersistableWorkout;

import static com.pij.zworkout.persistence.api.PersistableWorkout.EmptyString;

/**
 * <p>Created on 13/03/2018.</p>
 *
 * @author Pierrejean
 */

class PersistableWorkoutConverter {

    PersistableWorkout convert(Workout in) {
        PersistableWorkout result = new PersistableWorkout();
        result.name = EmptyString.create(in.name());
        return result;
    }
}
