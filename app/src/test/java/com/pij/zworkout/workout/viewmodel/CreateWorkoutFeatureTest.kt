package com.pij.zworkout.workout.viewmodel

import com.annimon.stream.Optional
import com.pij.zworkout.workout.Model
import junit.framework.TestCase.assertEquals
import org.junit.Test


/**
 *
 * Created on 01/03/2018.
 *
 * @author Pierrejean
 */
class CreateWorkoutFeatureTest {

    @Test
    fun `Adds an initial - empty - model`() {
        // given
        val current = Model.create(true, Optional.empty())
        val sut = CreateWorkoutFeature()

        // when
        val next = sut.apply(Any()).applyTo(current)

        // then
        assertEquals(next, Model.create(true, Optional.empty()))

    }

}