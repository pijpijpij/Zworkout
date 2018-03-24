package com.pij.zworkout.workout.viewmodel

import com.annimon.stream.Optional
import com.pij.zworkout.uc.Workout
import com.pij.zworkout.workout.State
import com.pij.zworkout.workout.StateTestUtil
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals


/**
 *
 * Created on 01/03/2018.
 *
 * @author Pierrejean
 */
class NameFeatureTest {

    private fun createState(workout: Workout, file: File? = null): State =
            StateTestUtil.empty().toBuilder().workout(workout).file(Optional.ofNullable<File>(file)).build()

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

}