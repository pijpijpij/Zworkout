package com.pij.zworkout.workout.viewmodel

import com.annimon.stream.Optional
import com.pij.zworkout.service.api.WorkoutFile
import com.pij.zworkout.service.api.WorkoutFileTestUtil
import com.pij.zworkout.uc.Workout
import com.pij.zworkout.workout.State
import com.pij.zworkout.workout.StateTestUtil
import org.junit.Assume.assumeTrue
import java.net.URI
import kotlin.test.Test
import kotlin.test.assertEquals


/**
 *
 * Created on 01/03/2018.
 *
 * @author Pierrejean
 */
class NameFeatureTest {

    private fun createState(workout: Workout, file: WorkoutFile = WorkoutFileTestUtil.empty()): State =
            StateTestUtil.empty().toBuilder().workout(workout).file(file).build()

    @Test
    fun `Reducer puts new name in the workout`() {
        // given
        val currentWorkout = Workout.EMPTY.name("the original")
        val current = createState(currentWorkout)
        val sut = NameFeature()

        // when
        val next = sut.process("the new one").reduce(current)

        // then
        assertEquals("the new one", next.workout().name())
    }

    @Test
    fun `Reducer changes file name if the file has not URI`() {
        // given
        val currentWorkout = Workout.EMPTY.name("the original")
        val current = createState(currentWorkout)
        assumeTrue(!current.file().uri().isPresent)
        val sut = NameFeature()

        // when
        val next = sut.process("the new one").reduce(current)

        // then
        assertEquals("the new one", next.file().name())
    }

    @Test
    fun `Reducer does not change file name if the file has a URI`() {
        // given
        val currentWorkout = Workout.EMPTY.name("the original")
        val currentFile = WorkoutFile.create(Optional.of(URI.create("http://something")), "the old one", Optional.empty())
        val current = createState(currentWorkout, currentFile)
        val sut = NameFeature()

        // when
        val next = sut.process("the new one").reduce(current)

        // then
        assertEquals("the old one", next.file().name())
    }

}