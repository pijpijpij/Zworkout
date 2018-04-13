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

import com.pij.utils.SysoutLogger
import com.pij.zworkout.uc.Effort
import com.pij.zworkout.uc.RelativePower
import com.pij.zworkout.uc.SteadyState
import com.pij.zworkout.uc.Workout
import com.pij.zworkout.workout.EffortProperty
import com.pij.zworkout.workout.State
import com.pij.zworkout.workout.SteadyStateCadenceValue
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * @author Pierrejean
 */
class EditEffortPropertyFeatureTest {

    private lateinit var sut: EditEffortPropertyFeature

    private lateinit var effort: Effort
    private lateinit var stateWithOneEffort: State

    private fun runSutOn(description: EffortProperty, state: State) = sut.process(description).reduce(state)

    @Before
    fun setUp() {
        effort = SteadyState(120, RelativePower(0.1f))
        stateWithOneEffort = State(workout = Workout(efforts = listOf(effort)))
        sut = EditEffortPropertyFeature(SysoutLogger())
    }

    @Test
    fun `Places the property value in transient property editEffortProperty`() {
        // given
        val value = SteadyStateCadenceValue(0, 90)

        // when
        val next = runSutOn(value, stateWithOneEffort)

        // then
        assertEquals(value, next.editEffortProperty)
    }


    @Test
    fun `Emits no propertyValue if value index out of range`() {
        // given
        val value = SteadyStateCadenceValue(1, 90)

        // when
        val next = runSutOn(value, stateWithOneEffort)

        // then
        assertNull(next.editEffortProperty)
    }

    @Test
    fun `Emits showError if value index out of range`() {
        // given
        val value = SteadyStateCadenceValue(1, 90)

        // when
        val next = runSutOn(value, stateWithOneEffort)

        // then
        assertNotNull(next.showError)
    }

}
