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

class NameFeature : Interaction<String, State> {

    override fun process(name: String): Reducer<State> {
        return Reducer { current ->
            current.toBuilder()
                    .workout(current.workout().name(name))
                    .build()
        }
    }
}
