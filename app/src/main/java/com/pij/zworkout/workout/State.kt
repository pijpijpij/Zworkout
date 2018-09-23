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

import com.pij.zworkout.uc.Workout
import java.io.File

/**
 *
 * Created on 01/03/2018.
 *
 * @author Pierrejean
 */
data class State(
        val inProgress: Boolean = false,
        val showError: String? = null,
        val editEffortProperty: EffortProperty? = null,
        val showSaved: Boolean = false,
        val workout: Workout = Workout(),
        val nameIsReadOnly: Boolean = false,
        val file: File? = null
)
