package com.pij.zworkout.workout.viewmodel

import com.pij.zworkout.workout.StateTestUtil
import kotlin.test.Test
import kotlin.test.assertEquals


/**
 *
 * Created on 01/03/2018.
 *
 * @author Pierrejean
 */
class CreateWorkoutFeatureTest {

    @Test
    fun `Sets the workout name on the model`() {
        // given
        val current = StateTestUtil.empty()
        val sut = CreateWorkoutFeature { "name" }

        // when
        val next = sut.process(Any()).reduce(current)

        // then
        assertEquals(next.workout().name(), "name")
    }

}