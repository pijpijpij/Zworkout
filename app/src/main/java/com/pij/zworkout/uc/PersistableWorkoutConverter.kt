/*
 * Copyright (c) 2018, Chiswick Forest
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

package com.pij.zworkout.uc

import com.pij.zworkout.persistence.api.*

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
                description = input.description,
                sportType = SportType.BIKE
        )
    }

    fun convert(input: PersistableWorkout): Workout {
        return with(input) {
            Workout(
                    name = name.value ?: "",
                    description = description ?: "",
                    efforts = efforts?.efforts?.map { convert(it) } ?: listOf()
            )
        }
    }

    private fun convert(input: PersistableEffort): Effort {
        return when (input) {
            is PersistableSteadyState -> SteadyState(duration = input.duration, power = input.power, cadence = input.cadence)
            else -> TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }


}