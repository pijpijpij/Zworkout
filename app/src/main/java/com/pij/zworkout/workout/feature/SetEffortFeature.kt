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
import com.pij.set
import com.pij.utils.Logger
import com.pij.zworkout.uc.Effort
import com.pij.zworkout.workout.ModelEffort
import com.pij.zworkout.workout.State

/**
 * @author Pierrejean
 */

internal class SetEffortFeature(
        private val logger: Logger,
        private val converter: (ModelEffort) -> Effort
) : Interaction<Pair<ModelEffort, Int>, State> {

    override fun process(event: Pair<ModelEffort, Int>): Reducer<State> {
        val (modelEffort, position) = event
        val newEffort = converter(modelEffort)
        return Reducer { current ->
            val source = current.workout.efforts
            try {
                val changed = source.set(position, newEffort)
                current.copy(workout = current.workout.copy(efforts = changed))
            } catch (e: IndexOutOfBoundsException) {
                logger.print(javaClass, e, "Could not replace effort at position $position with $modelEffort")
                current.copy(showError = e.message)
            }
        }
    }
}
