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

class NameFeature : Interaction<String, State> {

    /**
     * Also changes the name of the file if it does not exist.
     */
    override fun process(name: String): Reducer<State> {
        return Reducer { current ->
            val workout = current.toBuilder().workout(current.workout().name(name))
            val file = current.file()
            file.uri().executeIfAbsent { workout.file(file.toBuilder().name(name).build()) }
            workout.build()
        }
    }
}
