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

package com.pij.zworkout.uc

/**
 *
 * Created on 12/03/2018.
 *
 * @author Pierrejean
 */
data class Workout(
        val name: String = "",
        val description: String = "",
        val efforts: List<Effort> = listOf()
)

sealed class Effort
data class SteadyState(
        val duration: Int,
        val power: Power,
        val cadence: Int? = null
) : Effort()

data class Ramp(
        val duration: Int,
        val startPower: Power,
        val endPower: Power,
        val startCadence: Int? = null,
        val endCadence: Int? = null
) : Effort() {
    val up = startPower <= endPower
}

sealed class Power(open val relative: Float, open val input: String) {
    operator fun compareTo(rhs: Power): Int = relative.compareTo(rhs.relative)
}

data class BadPower(
        override val input: String,
        val inputError: String
) : Power(0f, input)

data class RelativePower(
        override val relative: Float,
        override val input: String
) : Power(relative, input) {
    constructor(relative: Float) : this(relative, relative.toString())
}

data class PowerRange(
        val range: Range,
        override val relative: Float = range.middle,
        override val input: String
) : Power(relative, input) {

    companion object {
        private const val topZ1 = 0.55f
        private const val topZ2 = 0.75f
        private const val topZ3 = 0.90f
        private const val topZ4 = 1.05f
        private const val topZ5 = 1.20f
    }

    enum class Range(range: ClosedFloatingPointRange<Float>) {
        Z1(0f.rangeTo(topZ1)),
        Z2(topZ1.rangeTo(topZ2)),
        Z3(topZ2.rangeTo(topZ3)),
        SweetSpot(topZ3.rangeTo(topZ3)),
        Z4(topZ3.rangeTo(topZ4)),
        Z5(topZ4.rangeTo(topZ5)),
        Z6(topZ5.rangeTo(Float.MAX_VALUE));

        val middle = (range.endInclusive + range.start) / 2

    }

}
