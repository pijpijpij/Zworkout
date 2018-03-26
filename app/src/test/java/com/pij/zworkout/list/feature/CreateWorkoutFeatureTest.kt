package com.pij.zworkout.list.feature

import com.annimon.stream.Optional
import com.pij.zworkout.list.Model
import com.pij.zworkout.list.WorkoutInfo
import junit.framework.TestCase.assertTrue
import kotlin.test.Test


/**
 *
 * Created on 01/03/2018.
 *
 * @author Pierrejean
 */
class CreateWorkoutFeatureTest {

    @Test
    fun `Adds the createWorkout flag`() {
        // given
        val workout = WorkoutInfo.create("id", "name", Optional.of("detail"))
        val current = Model.create(true, Optional.empty(), Optional.empty(), false, emptyList())
        val sut = CreateWorkoutFeature()

        // when
        val next = sut.process(workout).reduce(current)

        // then
        assertTrue(next.createWorkout())

    }

}