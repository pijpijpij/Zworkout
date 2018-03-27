package com.pij.zworkout.list.feature

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
        val workout = WorkoutInfo("id", "name", "detail")
        val current = Model(true, null, null, false, emptyList())
        val sut = CreateWorkoutFeature()

        // when
        val next = sut.process(workout).reduce(current)

        // then
        assertTrue(next.createWorkout)

    }

}