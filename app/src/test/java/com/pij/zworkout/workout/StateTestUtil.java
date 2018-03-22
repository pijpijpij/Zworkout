package com.pij.zworkout.workout;

import com.annimon.stream.Optional;
import com.pij.zworkout.service.api.WorkoutFileTestUtil;
import com.pij.zworkout.uc.Workout;

/**
 * <p>Created on 12/03/2018.</p>
 *
 * @author Pierrejean
 */
public class StateTestUtil {

    public static State empty() {
        return State.create(false, Optional.empty(), false, Workout.EMPTY, WorkoutFileTestUtil.empty());
    }
}