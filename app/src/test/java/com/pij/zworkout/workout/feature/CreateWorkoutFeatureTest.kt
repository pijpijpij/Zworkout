package com.pij.zworkout.workout.feature

import com.pij.zworkout.workout.State
import javax.inject.Provider
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.expect


/**
 *
 * Created on 01/03/2018.
 *
 * @author Pierrejean
 */
class CreateWorkoutFeatureTest {

    @Test
    fun `Applies the workout name to the model`() {
        // given
        val current = State()
        val sut = CreateWorkoutFeature(Provider { "name" })

        // when
        val next = sut.process(Any()).reduce(current)

        // then
        expect("name") { next.workout.name }
    }

    @Test
    fun `Defines initial file as empty`() {
        // given
        val current = State()
        val sut = CreateWorkoutFeature(Provider { "name" })

        // when
        val next = sut.process(Any()).reduce(current)

        // then
        assertNull(next.file)
    }

    @Test
    fun `Makes the name editable`() {
        // given
        val current = State()
        val sut = CreateWorkoutFeature(Provider { "name" })

        // when
        val next = sut.process(Any()).reduce(current)

        // then
        assertFalse(next.nameIsReadOnly)
    }

}