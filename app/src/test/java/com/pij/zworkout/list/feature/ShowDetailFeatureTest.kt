package com.pij.zworkout.list.feature

import com.pij.zworkout.list.Model
import com.pij.zworkout.list.WorkoutInfo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


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
        val workout = WorkoutInfo("id", "name", "detail")
        val current = Model(true, null, null, false, emptyList())
        val sut = ShowDetailFeature()

        // when
        val next = sut.process(workout).reduce(current)

        // then
        assertNotNull(next.showWorkout)

    }

    @Test
    fun `Adds the input workout as the showDetail flag`() {
        // given
        val workout = WorkoutInfo("id", "name", "detail")
        val current = Model(true, null, null, false, emptyList())
        val sut = ShowDetailFeature()

        // when
        val next = sut.process(workout).reduce(current)

        // then
        assertEquals(workout, next.showWorkout)

    }
}