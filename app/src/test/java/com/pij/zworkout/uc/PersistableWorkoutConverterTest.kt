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

import com.pij.zworkout.persistence.api.PersistableWorkout
import com.pij.zworkout.persistence.api.SportType
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
        val input = PersistableWorkout()
        input.name.value = ""
        input.description = null

        // when
        val result = sut.convert(input)

        // then
        assertTrue(result.description.isEmpty())
    }

    @Test
    fun `loads description`() {
        // given
        val input = PersistableWorkout()
        input.name.value = ""
        input.description = "hello!"

        // when
        val result = sut.convert(input)

        // then
        assertEquals("hello!", result.description)
    }
}