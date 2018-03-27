package com.pij.zworkout.list.feature

import com.pij.horrocks.Interaction
import com.pij.horrocks.Reducer
import com.pij.zworkout.list.Model

/**
 *
 * Created on 01/03/2018.
 *
 * @author Pierrejean
 */

class CreateWorkoutFeature : Interaction<Any, Model> {

    override fun process(event: Any): Reducer<Model> {
        return Reducer { it.copy(createWorkout = true) }
    }
}
