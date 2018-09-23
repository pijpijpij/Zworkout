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
 * <p>Created on 13/04/2018.</p>
 * @author Pierrejean
 */

sealed class EffortProperty(open val index: Int)

// Raw values
sealed class IntEffortProperty(override val index: Int, open val value: Int) : EffortProperty(index)

sealed class StringEffortProperty(override val index: Int, open val value: String) : EffortProperty(index)

// Type values
sealed class DurationProperty(override val index: Int, open val duration: Int) : IntEffortProperty(index, duration)

sealed class PowerProperty(override val index: Int, override val value: String) : StringEffortProperty(index, value)
sealed class CadenceProperty(override val index: Int, open val cadence: Int) : IntEffortProperty(index, cadence)

// Functional values
data class SteadyStateDurationValue(override val index: Int, override val duration: Int) : DurationProperty(index, duration)

data class SteadyStatePowerProperty(override val index: Int, val power: String) : PowerProperty(index, power)
data class SteadyStateCadenceValue(override val index: Int, override val cadence: Int) : CadenceProperty(index, cadence)

data class RampDurationValue(override val index: Int, override val duration: Int) : DurationProperty(index, duration)
data class RampStartPowerProperty(override val index: Int, val power: String) : PowerProperty(index, power)
data class RampEndPowerProperty(override val index: Int, val power: String) : PowerProperty(index, power)
data class RampStartCadenceValue(override val index: Int, override val cadence: Int) : CadenceProperty(index, cadence)
data class RampEndCadenceValue(override val index: Int, override val cadence: Int) : CadenceProperty(index, cadence)
