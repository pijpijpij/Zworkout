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

import com.pij.add
import com.pij.horrocks.Interaction
import com.pij.horrocks.Reducer
import com.pij.zworkout.uc.Effort
import com.pij.zworkout.workout.ModelEffort
import com.pij.zworkout.workout.State

/**
 * @author Pierrejean
 */

internal class InsertEffortFeature(private val converter: (ModelEffort) -> Effort) : Interaction<Pair<ModelEffort, Int>, State> {

    companion object {
        const val END_OF_LIST = -1
    }

    override fun process(event: Pair<ModelEffort, Int>): Reducer<State> {
        val (effort, position) = event
        val element = converter(effort)
        return Reducer { current ->
            val source = current.workout.efforts
            val augmented = if (position == END_OF_LIST) {
                source + element
            } else {
                source.add(position, element)
            }
            current.copy(workout = current.workout.copy(efforts = augmented))
        }
    }
}
