package com.pij.zworkout.uc;

import com.pij.zworkout.persistence.api.PersistableWorkout;

/**
 * <p>Created on 13/03/2018.</p>
 *
 * @author Pierrejean
 */

class PersistableWorkoutConverter {

    PersistableWorkout convert(Workout in) {
        PersistableWorkout result = new PersistableWorkout();
        result.setName(in.name());
        return result;
    }
}
