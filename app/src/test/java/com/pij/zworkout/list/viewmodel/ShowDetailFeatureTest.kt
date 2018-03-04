package com.pij.zworkout.list.viewmodel

import com.annimon.stream.Optional
import com.pij.zworkout.list.Model
import com.pij.zworkout.list.WorkoutDescriptor
import junit.framework.TestCase.assertTrue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Test


/**
 *
 * Created on 01/03/2018.
 *
 * @author Pierrejean
 */
class ShowDetailFeatureTest {

    @Test
    fun `Adds the showDetail flag`() {
        // given
        val workout = WorkoutDescriptor.create("id", "name", Optional.of("detail"))
        val current = Model.create(true, Optional.empty(), Optional.empty(), emptyList())
        val sut = ShowDetailFeature()

        // when
        val next = sut.apply(workout).applyTo(current)

        // then
        assertTrue(next.showWorkout().isPresent)

    }

    @Test
    fun `Adds the input workout as the showDetail flag`() {
        // given
        val workout = WorkoutDescriptor.create("id", "name", Optional.of("detail"))
        val current = Model.create(true, Optional.empty(), Optional.empty(), emptyList())
        val sut = ShowDetailFeature()

        // when
        val next = sut.apply(workout).applyTo(current)

        // then
        assertThat(next.showWorkout().get(), equalTo(workout))

    }
}