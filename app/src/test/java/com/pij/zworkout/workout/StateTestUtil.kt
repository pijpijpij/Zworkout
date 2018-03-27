package com.pij.zworkout.workout

import com.pij.zworkout.uc.Workout

/**
 *
 * Created on 12/03/2018.
 *
 * @author Pierrejean
 */
object StateTestUtil {

    fun empty(): State {
        return State(false, null, false, Workout.EMPTY, true, null)
    }
}