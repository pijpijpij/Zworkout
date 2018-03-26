package com.pij.zworkout.list.feature

import com.annimon.stream.Optional
import com.pij.horrocks.Interaction
import com.pij.horrocks.Reducer
import com.pij.zworkout.list.Model
import com.pij.zworkout.list.WorkoutInfo

/**
 *
 * Created on 01/03/2018.
 *
 * @author Pierrejean
 */

class ShowDetailFeature : Interaction<WorkoutInfo, Model> {

    override fun process(workoutInfo: WorkoutInfo): Reducer<Model> {
        return Reducer { current ->
            current.toBuilder()
                    .showWorkout(Optional.of(workoutInfo))
                    .build()
        }
    }
}
