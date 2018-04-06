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

import com.pij.zworkout.uc.*
import com.pij.zworkout.uc.PowerRange.Range

/**
 * <p>Created on 01/04/2018.</p>
 * @author Pierrejean
 */
internal class ModelConverter {

    fun convert(model: ModelEffort): Effort = model.toState()

    private fun ModelEffort.toState(): Effort {
        return when (this) {
            is ModelSteadyState -> toState()
            is ModelRamp -> toState()
        }
    }

    private fun ModelSteadyState.toState(): SteadyState =
            SteadyState(duration, power.toState(), cadence)

    private fun ModelRamp.toState(): Ramp =
            Ramp(duration, startPower.toState(), endPower.toState(), startCadence, endCadence)

    private fun ModelPower.toState(): Power {
        return when (this) {
            is ModelRelativePower -> RelativePower(fraction)
            is ModelRangedPower -> PowerRange(range.toState())
        }
    }

    private fun ModelPowerRange.toState(): PowerRange.Range {
        return when (this) {
            ModelPowerRange.Z1 -> Range.Z1
            ModelPowerRange.Z2 -> Range.Z2
            ModelPowerRange.Z3 -> Range.Z3
            ModelPowerRange.Z4 -> Range.Z4
            ModelPowerRange.Z5 -> Range.Z5
            ModelPowerRange.Z6 -> Range.Z6

            ModelPowerRange.SweetSpot -> Range.SweetSpot
        }
    }

}