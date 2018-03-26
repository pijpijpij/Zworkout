package com.pij.zworkout.workout;

import com.pij.zworkout.uc.Workout;

/**
 * <p>Created on 12/03/2018.</p>
 *
 * @author Pierrejean
 */
public class StateTestUtil {

    public static State empty() {
        return new State(false, null, false, Workout.Companion.getEMPTY(), true, null);
    }
}