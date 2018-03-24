package com.pij.zworkout.workout.viewmodel

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
            current.toBuilder()
                    .workout(current.workout().toBuilder().description(description).build())
                    .build()
        }
    }
}
