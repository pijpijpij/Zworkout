package com.pij.zworkout.uc

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.expect

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
    fun `copies workout name`() {
        // given
        val input = Workout.builder().name("hello").build()

        // when
        val result = sut.convert(input)

        // then
        expect("hello") { result.name.value }

    }
}