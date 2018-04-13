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

package com.pij.zworkout.workout

import com.pij.horrocks.StateConverter
import com.pij.zworkout.uc.Effort


/**
 *
 * Created on 01/03/2018.
 *
 * @author Pierrejean
 */

internal class WorkoutStateConverter(private val converter: (Effort) -> ModelEffort) : StateConverter<State, Model> {

    override fun convert(state: State): Model {
        return with(state) {
            Model(
                    inProgress = inProgress,
                    showSaved = showSaved,
                    showError = showError,
                    name = workout.name,
                    editEffortProperty = editEffortProperty,
                    nameIsReadOnly = nameIsReadOnly,
                    description = workout.description,
                    efforts = workout.efforts.map { converter.invoke(it) }
            )
        }
    }

}
