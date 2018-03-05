package com.pij.zworkout.list.viewmodel

import com.annimon.stream.Optional
import com.pij.zworkout.list.Model
import com.pij.zworkout.list.WorkoutInfo
import junit.framework.TestCase.assertTrue
import org.junit.Test


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
        val next = sut.apply(workout).applyTo(current)

        // then
        assertTrue(next.createWorkout())

    }

}