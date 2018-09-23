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

/**
 *
 * Created on 01/03/2018.
 *
 * @author Pierrejean
 */
data class Model(
        val inProgress: Boolean,
        val showError: String?,
        val showSaved: Boolean,
        val editEffortProperty: EffortProperty?,
        val name: String,
        val nameIsReadOnly: Boolean,
        val description: String,
        val efforts: List<ModelEffort> = listOf()
)

enum class ModelPowerRange {
    Z1, Z2, Z3, SweetSpot, Z4, Z5, Z6
}

sealed class ModelEffort

data class ModelSteadyState(
        val duration: Int,
        val power: String,
        val powerError: String? = null,
        val cadence: Int? = null
) : ModelEffort()

data class ModelRamp(
        val duration: Int,
        val startPower: ModelPower,
        val endPower: ModelPower,
        val startCadence: Int? = null,
        val endCadence: Int? = null
) : ModelEffort()

sealed class ModelPower
// TEMP DEBUG
//data class ModelBadPower(val text: String) : ModelPower()
//data class ModelRelativePower(val fraction: Float) : ModelPower()
data class ModelRangedPower(val range: ModelPowerRange) : ModelPower()

