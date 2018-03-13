package com.pij.zworkout.workout.viewmodel

import com.annimon.stream.Optional
import com.pij.zworkout.service.api.Workout
import com.pij.zworkout.service.api.WorkoutFileTestUtil
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
    fun `Reducer puts new name in the model`() {
        // given
        val currentWorkout = Workout.EMPTY.name("the original")
        val current = State.create(true, Optional.empty(), false, currentWorkout, WorkoutFileTestUtil.empty())
        val sut = NameFeature()

        // when
        val next = sut.process("the new one").reduce(current)

        // then
        assertEquals(next.workout().name(), "the new one")

    }

}