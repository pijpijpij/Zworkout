package com.pij.zworkout.workout.feature

import com.pij.horrocks.Interaction
import com.pij.horrocks.Reducer
import com.pij.zworkout.workout.State
import javax.inject.Provider

/**
 *
 * Created on 01/03/2018.
 *
 * @author Pierrejean
 */

class CreateWorkoutFeature(private val name: Provider<String>) : Interaction<Any, State> {

    override fun process(event: Any): Reducer<State> {
        return Reducer { current ->
            current.copy(workout = current.workout.copy(name = name.get()),
                    file = null,
                    nameIsReadOnly = false)
        }
    }

}
