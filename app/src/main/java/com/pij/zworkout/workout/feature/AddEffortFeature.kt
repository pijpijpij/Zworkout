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
import com.pij.zworkout.uc.SteadyState
import com.pij.zworkout.workout.State

/**
 * @author Pierrejean
 */

class AddEffortFeature : Interaction<Any, State> {

    override fun process(event: Any): Reducer<State> {
        val element = SteadyState(5, 1f)
        return Reducer { current ->
            current.copy(workout = current.workout.copy(efforts = current.workout.efforts + element))
        }
    }

}
