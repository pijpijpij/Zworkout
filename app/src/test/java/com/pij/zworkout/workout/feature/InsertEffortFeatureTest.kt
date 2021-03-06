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

package com.pij.zworkout.workout.feature

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.pij.zworkout.uc.*
import com.pij.zworkout.workout.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.junit.Before
import org.junit.Test

/**
 *
 * Created on 31/03/2018.
 *
 * @author Pierrejean
 */
class InsertEffortFeatureTest {

    private lateinit var sut: InsertEffortFeature
    private lateinit var converterMock: (ModelEffort) -> Effort

    private lateinit var effort: ModelEffort

    private lateinit var converted: Effort

    @Before
    fun setUp() {
        effort = ModelSteadyState(120, "Z1", null, 90)
        converted = SteadyState(1, RelativePower(1f), 1)
        converterMock = mock()
        sut = InsertEffortFeature(converterMock)
    }

    @Test
    fun `Adds a Steady state to an empty list of efforts`() {
        // given
        val current = State()
        whenever(converterMock.invoke(effort)).thenReturn(converted)

        // when
        val next = sut.process(Pair(effort, InsertEffortFeature.END_OF_LIST)).reduce(current)

        // then
        assertThat(next.workout.efforts, contains<Effort>(converted))
    }

    @Test
    fun `Appends a Steady state to a non-empty list of efforts`() {
        // given
        val current = State(workout = Workout(efforts = listOf<Effort>(SteadyState(120, RelativePower(0.1f)))))
        whenever(converterMock.invoke(effort)).thenReturn(converted)

        // when
        val next = sut.process(Pair(effort, InsertEffortFeature.END_OF_LIST)).reduce(current)

        // then
        assertThat(next.workout.efforts, contains<Effort>(SteadyState(120, RelativePower(0.1f)), converted))
    }

    @Test
    fun `Inserts a Steady state in a non-empty list of efforts`() {
        // given
        val current = State(workout = Workout(efforts = listOf<Effort>(SteadyState(120, RelativePower(0.1f)))))
        whenever(converterMock.invoke(effort)).thenReturn(converted)

        // when
        val next = sut.process(Pair(effort, 0)).reduce(current)

        // then
        assertThat(next.workout.efforts, contains<Effort>(converted, SteadyState(120, RelativePower(0.1f))))
    }

    @Test
    fun `Adds a Ramp to an empty list of efforts`() {
        // given
        val effort = ModelRamp(120, ModelRangedPower(ModelPowerRange.Z1), ModelRangedPower(ModelPowerRange.Z2), 90, 100)
        val current = State()
        val converted = Ramp(1, RelativePower(1f), RelativePower(2f), 5, 10)
        whenever(converterMock.invoke(effort)).thenReturn(converted)

        // when
        val next = sut.process(Pair(effort, InsertEffortFeature.END_OF_LIST)).reduce(current)

        // then
        assertThat(next.workout.efforts, contains<Effort>(converted))
    }

}
