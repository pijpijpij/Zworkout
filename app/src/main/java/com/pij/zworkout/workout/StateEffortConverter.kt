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

import com.pij.zworkout.uc.BadPower
import com.pij.zworkout.uc.Effort
import com.pij.zworkout.uc.Power
import com.pij.zworkout.uc.SteadyState

/**
 * <p>Created on 01/04/2018.</p>
 * @author Pierrejean
 */
internal class StateEffortConverter : (Effort) -> ModelEffort {

    override fun invoke(model: Effort): ModelEffort = model.toModel()

    private fun Effort.toModel(): ModelEffort {
        return when (this) {
            is SteadyState -> ModelSteadyState(duration, power.toModel(), power.toError(), cadence)
//            is Ramp -> ModelRamp(duration, startPower.toModel(), endPower.toModel(), startCadence, endCadence)
            else -> TODO("Not implemented yet")
        }
    }

    private fun Power.toModel(): String = input

    private fun Power.toError(): String? = when {
        this is BadPower -> inputError
        else -> null
    }

//    private fun Range.toModel(): ModelPowerRange {
//        return when (this) {
//            Range.Z1 -> ModelPowerRange.Z1
//            Range.Z2 -> ModelPowerRange.Z2
//            Range.Z3 -> ModelPowerRange.Z3
//            Range.Z4 -> ModelPowerRange.Z4
//            Range.Z5 -> ModelPowerRange.Z5
//            Range.Z6 -> ModelPowerRange.Z6
//
//            Range.SweetSpot -> ModelPowerRange.SweetSpot
//        }
//    }

}
