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

import com.pij.zworkout.uc.Effort
import com.pij.zworkout.uc.SteadyState
import com.pij.zworkout.uc.Workout
import com.pij.zworkout.workout.State
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.junit.Test

/**
 *
 * Created on 31/03/2018.
 *
 * @author Pierrejean
 */
class AddEffortFeatureTest {

    @Test
    fun `Adds a Steady state to an empty list of efforts`() {
        // given
        val current = State()
        val sut = AddEffortFeature()

        // when
        val next = sut.process(Any()).reduce(current)

        // then
        assertThat(next.workout.efforts, contains<Effort>(SteadyState(5, 1f)))
    }

    @Test
    fun `Appends a Steady state to a non-empty list of efforts`() {
        // given
        val current = State(workout = Workout(efforts = listOf<Effort>(SteadyState(120, 0.1f))))
        val sut = AddEffortFeature()

        // when
        val next = sut.process(Any()).reduce(current)

        // then
        assertThat(next.workout.efforts, contains<Effort>(SteadyState(120, 0.1f), SteadyState(5, 1f)))
    }

}