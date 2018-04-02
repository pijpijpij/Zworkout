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

package com.pij.zworkout.persistence.api

import org.simpleframework.xml.*

/**
 *
 * Created on 12/03/2018.
 *
 * @author Pierrejean
 */
@Root(name = "workout_file", strict = false)
data class PersistableWorkout(

        @field:Element var name: EmptyString = EmptyString(),
        @field:Element(required = false) var description: String? = null,
        @field:Element(required = false) var sportType: SportType? = null,
        // TODO add Tags
        @field:Element(name = "workout", required = false) var efforts: PersistableEfforts? = null
)

data class EmptyString(

        @field:Text(required = false) var value: String? = null
)

enum class SportType {
    BIKE
}

/**
 * Field is {@link #efforts} declared immutable, but it needs to be mutable for the XML library to do its thing.
 */
@Root(name = "workout", strict = false)
data class PersistableEfforts(
        @field:ElementListUnion(
                ElementList(required = false, inline = true, type = PersistableSteadyState::class),
                ElementList(required = false, inline = true, type = PersistableRamp::class),
                ElementList(required = false, inline = true, type = PersistableCoolDown::class)
        ) var efforts: List<PersistableEffort> = mutableListOf()
)

/**
 * Artificial base class for all efforts.
 */
sealed class PersistableEffort

@Root(name = "SteadyState", strict = false)
data class PersistableSteadyState(
        @field:Attribute(name = "Duration") var duration: Int = 0,
        @field:Attribute(name = "Power") var power: Float = 0f,
        @field:Attribute(name = "Cadence", required = false) var cadence: Int? = null
) : PersistableEffort()

@Root(name = "Ramp", strict = false)
data class PersistableRamp(
        @field:Attribute(name = "Duration") var duration: Int = 0,
        @field:Attribute(name = "PowerLow") var startPower: Float = 0f,
        @field:Attribute(name = "PowerHigh") var endPower: Float = 0f,
        @field:Attribute(name = "CadenceResting", required = false) var startCadence: Int? = null,
        @field:Attribute(name = "Cadence", required = false) var endCadence: Int? = null
) : PersistableEffort()

@Root(name = "Cooldown", strict = false)
data class PersistableCoolDown(
        @field:Attribute(name = "Duration") var duration: Int = 0,
        @field:Attribute(name = "PowerLow") var startPower: Float = 0f,
        @field:Attribute(name = "PowerHigh") var endPower: Float = 0f,
        @field:Attribute(name = "Cadence", required = false) var startCadence: Int? = null,
        // TODO check 'cadence resting' exists and is the cadence at the end of the effort
        @field:Attribute(name = "CadenceResting", required = false) var endCadence: Int? = null
) : PersistableEffort()