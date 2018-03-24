package com.pij.zworkout.workout.viewmodel

import com.annimon.stream.Optional
import com.pij.zworkout.workout.StateTestUtil
import kotlin.test.Test
import kotlin.test.expect


/**
 *
 * Created on 01/03/2018.
 *
 * @author Pierrejean
 */
class CreateWorkoutFeatureTest {

    @Test
    fun `Applies the workout name to the model`() {
        // given
        val current = StateTestUtil.empty()
        val sut = CreateWorkoutFeature { "name" }

        // when
        val next = sut.process(Any()).reduce(current)

        // then
        expect("name") { next.workout().name() }
    }

    @Test
    fun `Defines initial file name`() {
        // given
        val current = StateTestUtil.empty()
        val sut = CreateWorkoutFeature { "name" }

        // when
        val next = sut.process(Any()).reduce(current)

        // then
        expect("name") { next.file().name() }
    }

    @Test
    fun `Defines initial URI as empty`() {
        // given
        val current = StateTestUtil.empty()
        val sut = CreateWorkoutFeature { "name" }

        // when
        val next = sut.process(Any()).reduce(current)

        // then
        expect(Optional.empty()) { next.file().uri() }
    }

}