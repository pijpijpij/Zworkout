package com.pij.zworkout.workout.viewmodel

import com.annimon.stream.Optional
import com.pij.horrocks.Interaction
import com.pij.horrocks.Reducer
import com.pij.zworkout.workout.State
import java.io.File

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
            current.toBuilder()
                    .workout(current.workout().name(name.get()))
                    .file(Optional.empty<File>())
                    .nameIsReadOnly(false)
                    .build()
        }
    }

}
