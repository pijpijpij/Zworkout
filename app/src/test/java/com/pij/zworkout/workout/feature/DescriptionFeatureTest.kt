package com.pij.zworkout.workout.feature

import com.pij.zworkout.uc.Workout
import com.pij.zworkout.workout.StateTestUtil
import kotlin.test.Test
import kotlin.test.assertEquals


/**
 *
 * Created on 01/03/2018.
 *
 * @author Pierrejean
 */
class DescriptionFeatureTest {

    @Test
    fun `Reducer puts new description in the workout`() {
        // given
        val currentWorkout = Workout.EMPTY.toBuilder().description("the original").build()
        val current = StateTestUtil.empty().withWorkout(currentWorkout)
        val sut = DescriptionFeature()

        // when
        val next = sut.process("the new one").reduce(current)

        // then
        assertEquals("the new one", next.workout().description())
    }

}