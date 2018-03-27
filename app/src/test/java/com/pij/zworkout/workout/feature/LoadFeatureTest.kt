package com.pij.zworkout.workout.feature

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.pij.utils.SysoutLogger
import com.pij.zworkout.list.WorkoutInfo
import com.pij.zworkout.service.api.WorkoutFile
import com.pij.zworkout.uc.Workout
import com.pij.zworkout.uc.WorkoutPersistenceUC
import com.pij.zworkout.workout.State
import com.pij.zworkout.workout.StateTestUtil
import io.reactivex.Single
import org.mockito.Mockito.`when`
import java.io.File
import java.net.URI
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 *
 * Created on 02/01/2018.
 *
 * @author PierreJean
 */
class LoadFeatureTest {

    private lateinit var workoutPersistenceMock: WorkoutPersistenceUC

    private val loadedWorkout = Workout.EMPTY.copy(name = "just loaded")
    private val defaultState: State = StateTestUtil.empty().copy(workout = Workout.EMPTY)
    private val defaultWorkoutId = "file:///workout/id"

    private lateinit var sut: LoadFeature
    private lateinit var workoutFile: WorkoutFile
    private lateinit var workoutInfo: WorkoutInfo

    @BeforeTest
    fun setUp() {
        workoutPersistenceMock = mock()
        `when`(workoutPersistenceMock.load(any())).thenReturn(Single.never())
        workoutFile = WorkoutFile(URI.create("some/file"), "zip")
        workoutInfo = WorkoutInfo("some/file", "zip", null)
        sut = LoadFeature(SysoutLogger(), workoutPersistenceMock, "the default error message")
    }

    private fun runSutOn(workoutId: String, state: State) = sut.process(workoutId).map { it.reduce(state) }

    @Test
    fun `Before store provides workout, sut emits in Progress`() {
        // given

        // when
        val state = runSutOn(defaultWorkoutId, defaultState)
                .take(1)
                .map { it.inProgress }
                .test()

        // then
        state.assertValue(true)
    }

    @Test
    fun `When workoutId is invalid, sut emits showError`() {
        // given

        // when
        val state = runSutOn("not a valid id", defaultState)
                .skip(1)
                .test()

        // then
        state.assertValue { it.showError != null }
    }

    @Test
    fun `After store provides workout, sut emits not in Progress`() {
        // given
        `when`(workoutPersistenceMock.load(any())).thenReturn(Single.just(loadedWorkout))

        // when
        val state = runSutOn(defaultWorkoutId, defaultState)
                .skip(1)
                .map { it.inProgress }
                .test()

        // then
        state.assertValue(false)
    }

    @Test
    fun `After store provides workout, sut emits file`() {
        // given
        `when`(workoutPersistenceMock.load(any())).thenReturn(Single.just(loadedWorkout))

        // when
        val state = runSutOn(defaultWorkoutId, defaultState)
                .skip(1)
                .map { it.file }
                .test()

        // then
        state.assertValue(File(URI.create(defaultWorkoutId)))
    }

    @Test
    fun `After store provides workout, sut emits workout`() {
        // given
        `when`(workoutPersistenceMock.load(any())).thenReturn(Single.just(loadedWorkout))

        // when
        val state = runSutOn(defaultWorkoutId, defaultState)
                .skip(1)
                .map { it.workout }
                .test()

        // then
        state.assertValue(loadedWorkout)
    }

    @Test
    fun `After store provides workout, sut completes`() {
        // given
        `when`(workoutPersistenceMock.load(any())).thenReturn(Single.just(loadedWorkout))

        // when
        val observer = runSutOn(defaultWorkoutId, defaultState).test()

        // then
        observer.assertComplete()
    }

    @Test
    fun `After store fails to provide workout, sut emits not in Progress`() {
        // given
        `when`(workoutPersistenceMock.load(any())).thenReturn(Single.error(IllegalAccessException("the error message")))

        // when
        val state = runSutOn(defaultWorkoutId, defaultState)
                .skip(1)
                .map { it.inProgress }
                .test()

        // then
        state.assertValue(false)
    }

    @Test
    fun `When store fails to provide workout with a message, sut emits Failure with the exception message`() {
        // given
        `when`(workoutPersistenceMock.load(any())).thenReturn(Single.error(IllegalAccessException("the error message")))

        // when
        val state = runSutOn(defaultWorkoutId, defaultState)
                .skip(1)
                .map { it.showError }
                .test()

        // then
        state.assertValue("the error message")
    }

    @Test
    fun `When store fails to provide workout with an empty message, sut emits Failure with the default message`() {
        // given
        `when`(workoutPersistenceMock.load(any())).thenReturn(Single.error(IllegalAccessException()))

        // when
        val state = runSutOn(defaultWorkoutId, defaultState)
                .skip(1)
                .map { it.showError }
                .test()

        // then
        state.assertValue("the default error message")
    }

    @Test
    fun `When store fails to provide workout, sut completes`() {
        // given
        `when`(workoutPersistenceMock.load(any())).thenReturn(Single.error(IllegalAccessException("the error message")))

        // when
        val observer = runSutOn(defaultWorkoutId, defaultState).test()

        // then
        observer.assertComplete()
    }

}