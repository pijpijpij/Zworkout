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
import com.pij.utils.SysoutLogger
import com.pij.zworkout.uc.Effort
import com.pij.zworkout.uc.RelativePower
import com.pij.zworkout.uc.SteadyState
import com.pij.zworkout.uc.Workout
import com.pij.zworkout.workout.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotNull

/**
 * @author Pierrejean
 */
class SetEffortFeatureTest {

    private lateinit var sut: SetEffortFeature
    private lateinit var converterMock: (ModelEffort) -> Effort

    private lateinit var effort: ModelEffort

    private lateinit var converted: Effort

    private fun runSutOn(effort: ModelEffort, position: Int, state: State) = sut.process(Pair(effort, position)).reduce(state)

    @Before
    fun setUp() {
        effort = ModelSteadyState(120, ModelRangedPower(ModelPowerRange.Z1), 90)
        converted = SteadyState(1, RelativePower(1f), 1)
        converterMock = mock()
        sut = SetEffortFeature(SysoutLogger(), converterMock)
    }

    @Test
    fun `Setting a Steady state to an empty list of efforts emits failure`() {
        // given
        val current = State()
        whenever(converterMock.invoke(effort)).thenReturn(converted)

        // when
        val next = runSutOn(effort, 0, current)

        // then
        assertNotNull(next.showError)
    }

    @Test
    fun `Sets a Steady state in a non-empty list of efforts`() {
        // given
        val current = State(workout = Workout(efforts = listOf<Effort>(SteadyState(120, RelativePower(0.1f)))))
        whenever(converterMock.invoke(effort)).thenReturn(converted)

        // when
        val next = runSutOn(effort, 0, current)

        // then
        assertThat(next.workout.efforts, contains<Effort>(converted))
    }

}
