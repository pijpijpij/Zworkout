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
import com.pij.zworkout.uc.Effort
import com.pij.zworkout.uc.Ramp
import com.pij.zworkout.uc.SteadyState
import com.pij.zworkout.uc.Workout
import com.pij.zworkout.workout.ModelConverter
import com.pij.zworkout.workout.PowerRange
import com.pij.zworkout.workout.State
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
    private lateinit var converterMock: ModelConverter

    @Before
    fun setUp() {
        converterMock = mock()
        sut = InsertEffortFeature(converterMock)
    }

    @Test
    fun `Adds a Steady state to an empty list of efforts`() {
        // given
        val effort = com.pij.zworkout.workout.SteadyState(120, PowerRange.Z1, 90)
        val current = State()
        val converted = SteadyState(1, 1f, 1)
        whenever(converterMock.convert(effort)).thenReturn(converted)

        // when
        val next = sut.process(Pair(effort, InsertEffortFeature.END_OF_LIST)).reduce(current)

        // then
        assertThat(next.workout.efforts, contains<Effort>(converted))
    }

    @Test
    fun `Appends a Steady state to a non-empty list of efforts`() {
        // given
        val effort = com.pij.zworkout.workout.SteadyState(120, PowerRange.Z1, 90)
        val current = State(workout = Workout(efforts = listOf<Effort>(SteadyState(120, 0.1f))))
        val converted = SteadyState(1, 1f, 1)
        whenever(converterMock.convert(effort)).thenReturn(converted)

        // when
        val next = sut.process(Pair(effort, InsertEffortFeature.END_OF_LIST)).reduce(current)

        // then
        assertThat(next.workout.efforts, contains<Effort>(SteadyState(120, 0.1f), converted))
    }

    @Test
    fun `Inserts a Steady state in a non-empty list of efforts`() {
        // given
        val effort = com.pij.zworkout.workout.SteadyState(120, PowerRange.Z1, 90)
        val current = State(workout = Workout(efforts = listOf<Effort>(SteadyState(120, 0.1f))))
        val converted = SteadyState(1, 1f, 1)
        whenever(converterMock.convert(effort)).thenReturn(converted)

        // when
        val next = sut.process(Pair(effort, 0)).reduce(current)

        // then
        assertThat(next.workout.efforts, contains<Effort>(converted, SteadyState(120, 0.1f)))
    }

    @Test
    fun `Adds a Ramp to an empty list of efforts`() {
        // given
        val effort = com.pij.zworkout.workout.Ramp(120, PowerRange.Z1, PowerRange.Z2, 90, 100)
        val current = State()
        val converted = Ramp(1, 1f, 2f, 5, 10)
        whenever(converterMock.convert(effort)).thenReturn(converted)

        // when
        val next = sut.process(Pair(effort, InsertEffortFeature.END_OF_LIST)).reduce(current)

        // then
        assertThat(next.workout.efforts, contains<Effort>(converted))
    }

}
