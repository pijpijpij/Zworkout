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

package com.pij.zworkout.workout.feature

import com.pij.horrocks.Interaction
import com.pij.horrocks.Reducer
import com.pij.utils.Logger
import com.pij.zworkout.workout.EffortPropertyEvent
import com.pij.zworkout.workout.State
import com.pij.zworkout.workout.SteadyStatePowerEvent

/**
 * @author Pierrejean
 */

internal class EditEffortPropertyFeature(
        private val logger: Logger
) : Interaction<EffortPropertyEvent, State> {

    override fun process(event: EffortPropertyEvent): Reducer<State> {
        return Reducer { current ->
            when (event) {
                is SteadyStatePowerEvent -> println("PJC asked to edit '$event.power' at ${event.index}")
            }
            current
        }
    }
}
