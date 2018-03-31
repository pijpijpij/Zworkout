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

import com.pij.zworkout.persistence.api.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Created on 16/03/2018.
 *
 * @author Pierrejean
 */
class PersistableWorkoutConverterTest {

    private lateinit var sut: PersistableWorkoutConverter

    @BeforeTest
    fun setUp() {
        sut = PersistableWorkoutConverter()
    }

    @Test
    fun `persists workout name`() {
        // given
        val input = Workout(name = "hello")

        // when
        val result = sut.convert(input)

        // then
        assertEquals("hello", result.name.value)
    }

    @Test
    fun `persists description`() {
        // given
        val input = Workout(description = "hello")

        // when
        val result = sut.convert(input)

        // then
        assertEquals("hello", result.description)
    }

    @Test
    fun `persists bike as sportType`() {
        // given
        val input = Workout()

        // when
        val result = sut.convert(input)

        // then
        assertEquals(SportType.BIKE, result.sportType)
    }

    @Test
    fun `loads workout name`() {
        // given
        val input = PersistableWorkout()
        input.name.value = "hello!"

        // when
        val result = sut.convert(input)

        // then
        assertEquals("hello!", result.name)
    }

    @Test
    fun `loads null description as empty`() {
        // given
        val input = PersistableWorkout(EmptyString(""))

        // when
        val result = sut.convert(input)

        // then
        assertTrue(result.description.isEmpty())
    }

    @Test
    fun `loads description`() {
        // given
        val input = PersistableWorkout(EmptyString(""), "hello!")

        // when
        val result = sut.convert(input)

        // then
        assertEquals("hello!", result.description)
    }

    @Test
    fun `loads steady state effort`() {
        // given
        val effort = PersistableSteadyState(120, 0.90f, 97)
        val input = PersistableWorkout(EmptyString(""), "hello!", efforts = PersistableEfforts(listOf(effort)))

        // when
        val result = sut.convert(input)

        // then
        assertThat(result.efforts, contains<Effort>(SteadyState(120, 0.9f, 97)))
    }
}