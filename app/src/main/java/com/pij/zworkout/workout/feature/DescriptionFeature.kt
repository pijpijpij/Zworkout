package com.pij.zworkout.workout.feature

import com.pij.horrocks.Interaction
import com.pij.horrocks.Reducer
import com.pij.zworkout.workout.State

/**
 *
 * Created on 01/03/2018.
 *
 * @author Pierrejean
 */

class DescriptionFeature : Interaction<String, State> {

    override fun process(description: String): Reducer<State> {
        return Reducer { current ->
            current.copy(workout = current.workout.copy(description = description))
        }
    }
}
