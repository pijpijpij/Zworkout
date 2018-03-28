package com.pij.zworkout.uc

import com.pij.zworkout.persistence.api.PersistableWorkout
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