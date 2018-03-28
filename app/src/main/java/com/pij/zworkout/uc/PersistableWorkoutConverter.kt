package com.pij.zworkout.uc

import com.pij.zworkout.persistence.api.EmptyString
import com.pij.zworkout.persistence.api.PersistableWorkout

/**
 *
 * Created on 13/03/2018.
 *
 * @author Pierrejean
 */

internal class PersistableWorkoutConverter {

    fun convert(input: Workout): PersistableWorkout {
        return PersistableWorkout(
                name = EmptyString(input.name),
                description = input.description
        )
    }

    fun convert(input: PersistableWorkout): Workout {
        return Workout(
                name = input.name.value ?: "",
                description = input.description ?: ""
        )
    }
}
