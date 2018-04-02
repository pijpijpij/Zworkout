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
 * <p>Created on 01/04/2018.</p>
 * @author Pierrejean
 */
internal class ModelConverter {

    fun convert(model: Effort): com.pij.zworkout.uc.Effort = model.toState()

}

private fun Effort.toState(): com.pij.zworkout.uc.Effort {
    return when (this) {
        is Ramp -> toState()
        is SteadyState -> toState()
        else -> TODO("Effort not supported $this")
    }
}

private fun Ramp.toState(): com.pij.zworkout.uc.Ramp =
        com.pij.zworkout.uc.Ramp(duration, startPower.toState(), endPower.toState(), startCadence, endCadence)

private fun SteadyState.toState(): com.pij.zworkout.uc.SteadyState =
        com.pij.zworkout.uc.SteadyState(duration, power.toState(), cadence)

private fun PowerRange.toState(): Float = 1.0f
