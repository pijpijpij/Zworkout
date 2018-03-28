package com.pij.zworkout.workout.feature

import com.pij.zworkout.uc.Workout
import com.pij.zworkout.workout.State
import kotlin.test.Test
import kotlin.test.assertEquals


/**
 *
 * Created on 01/03/2018.
 *
 * @author Pierrejean
 */
class NameFeatureTest {

    @Test
    fun `Reducer puts new name in the workout`() {
        // given
        val currentWorkout = Workout(name = "the original")
        val current = State().copy(workout = currentWorkout)
        val sut = NameFeature()

        // when
        val next = sut.process("the new one").reduce(current)

        // then
        assertEquals("the new one", next.workout.name)
    }

}