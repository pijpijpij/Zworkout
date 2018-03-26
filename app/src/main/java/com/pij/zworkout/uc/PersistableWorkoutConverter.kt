package com.pij.zworkout.uc

import com.pij.zworkout.persistence.api.PersistableWorkout
import com.pij.zworkout.persistence.api.PersistableWorkout.EmptyString

/**
 *
 * Created on 13/03/2018.
 *
 * @author Pierrejean
 */

internal class PersistableWorkoutConverter {

    fun convert(input: Workout): PersistableWorkout {
        val result = PersistableWorkout()
        result.name = EmptyString.create(input.name)
        result.description = input.description
        return result
    }

    fun convert(input: PersistableWorkout): Workout {
        return Workout(
                name = input.name.value ?: "",
                description = input.description ?: ""
        )
    }
}
