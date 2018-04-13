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

/**
 * <p>Created on 01/04/2018.</p>
 * @author Pierrejean
 */
internal class ModelEffortConverter : (ModelEffort) -> Effort {

    override fun invoke(model: ModelEffort): Effort =
            when (model) {
                is ModelSteadyState -> model.toState()
//                is ModelRamp -> model.toState()
                else -> TODO("Not implemented yet")
            }

    private fun ModelSteadyState.toState(): SteadyState {
        return SteadyState(duration, power.toPower(), cadence)
    }

    private fun String.toPower(): Power {
        return try {
            val range = PowerRange.Range.valueOf(this)
            PowerRange(range, input = this)
        } catch (e: IllegalArgumentException) {
            try {
                val fraction = toFloat()
                RelativePower(fraction, this)
            } catch (e: NumberFormatException) {
                BadPower(this, e.message ?: "unexpected value")
            }
        }
    }

//    private fun ModelRamp.toState(): Ramp =
//            Ramp(duration, startPower.toState(), endPower.toState(), startCadence, endCadence)
//
//    private fun String.toStatePower(): Power {
//        return when (this) {
//            is ModelRelativePower -> RelativePower(fraction)
//            is ModelRangedPower -> PowerRange(range.toState())
//            is ModelBadPower -> RelativePower(0f, input = text, inputError = "Not a valid power")
//        }
//    }
//
//    private fun ModelPowerRange.toState(): PowerRange.Range = when (this) {
//        ModelPowerRange.Z1 -> Range.Z1
//        ModelPowerRange.Z2 -> Range.Z2
//        ModelPowerRange.Z3 -> Range.Z3
//        ModelPowerRange.Z4 -> Range.Z4
//        ModelPowerRange.Z5 -> Range.Z5
//        ModelPowerRange.Z6 -> Range.Z6
//
//        ModelPowerRange.SweetSpot -> Range.SweetSpot
//    }

}

