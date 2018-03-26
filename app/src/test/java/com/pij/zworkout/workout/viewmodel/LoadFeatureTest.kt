package com.pij.zworkout.workout.viewmodel

import com.annimon.stream.Optional
import com.nhaarman.mockitokotlin2.mock
import com.pij.utils.SysoutLogger
import com.pij.zworkout.list.WorkoutInfo
import com.pij.zworkout.service.api.WorkoutFile
import com.pij.zworkout.uc.Workout
import com.pij.zworkout.uc.WorkoutPersistenceUC
import com.pij.zworkout.workout.State
import com.pij.zworkout.workout.StateTestUtil
import io.reactivex.Single
import org.mockito.ArgumentMatchers.any
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

    private val loadedWorkout = Workout.EMPTY.name("just loaded")
    private val defaultState: State = StateTestUtil.empty().withWorkout(Workout.EMPTY)
    private val defaultWorkoutId = "file:///workout/id"

    private lateinit var sut: LoadFeature
    private lateinit var workoutFile: WorkoutFile
    private lateinit var workoutInfo: WorkoutInfo

    @BeforeTest
    fun setUp() {
        workoutPersistenceMock = mock()
        `when`(workoutPersistenceMock.load(any<File>())).thenReturn(Single.never())
        workoutFile = WorkoutFile.create(URI.create("some/file"), "zip")
        workoutInfo = WorkoutInfo.create("some/file", "zip", Optional.empty())
        sut = LoadFeature(SysoutLogger(), workoutPersistenceMock, "the default error message")
    }

    private fun runSutOn(workoutId: String, state: State) = sut.process(workoutId).map { result -> result.reduce(state) }

    @Test
    fun `Before store provides workout, sut emits in Progress`() {
        // given

        // when
        val state = runSutOn(defaultWorkoutId, defaultState)
                .take(1)
                .map { it.inProgress() }
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
                .map { it.showError() }
                .test()

        // then
        state.assertValue { it.isPresent }
    }

    @Test
    fun `After store provides workout, sut emits not in Progress`() {
        // given
        `when`(workoutPersistenceMock.load(any<File>())).thenReturn(Single.just(loadedWorkout))

        // when
        val state = runSutOn(defaultWorkoutId, defaultState)
                .skip(1)
                .map { it.inProgress() }
                .test()

        // then
        state.assertValue(false)
    }

    @Test
    fun `After store provides workout, sut emits file`() {
        // given
        `when`(workoutPersistenceMock.load(any<File>())).thenReturn(Single.just(loadedWorkout))

        // when
        val state = runSutOn(defaultWorkoutId, defaultState)
                .skip(1)
                .map { it.file().get() }
                .test()

        // then
        state.assertValue(File(URI.create(defaultWorkoutId)))
    }

    @Test
    fun `After store provides workout, sut emits workout`() {
        // given
        `when`(workoutPersistenceMock.load(any<File>())).thenReturn(Single.just(loadedWorkout))

        // when
        val state = runSutOn(defaultWorkoutId, defaultState)
                .skip(1)
                .map { it.workout() }
                .test()

        // then
        state.assertValue(loadedWorkout)
    }

    @Test
    fun `After store provides workout, sut completes`() {
        // given
        `when`(workoutPersistenceMock.load(any<File>())).thenReturn(Single.just(loadedWorkout))

        // when
        val observer = runSutOn(defaultWorkoutId, defaultState).test()

        // then
        observer.assertComplete()
    }

    @Test
    fun `After store fails to provide workout, sut emits not in Progress`() {
        // given
        `when`(workoutPersistenceMock.load(any<File>())).thenReturn(Single.error(IllegalAccessException("the error message")))

        // when
        val state = runSutOn(defaultWorkoutId, defaultState)
                .skip(1)
                .map { it.inProgress() }
                .test()

        // then
        state.assertValue(false)
    }

    @Test
    fun `When store fails to provide workout with a message, sut emits Failure with the exception message`() {
        // given
        `when`(workoutPersistenceMock.load(any<File>())).thenReturn(Single.error(IllegalAccessException("the error message")))

        // when
        val state = runSutOn(defaultWorkoutId, defaultState)
                .skip(1)
                .map { it.showError().get() }
                .test()

        // then
        state.assertValue("the error message")
    }

    @Test
    fun `When store fails to provide workout with an empty message, sut emits Failure with the default message`() {
        // given
        `when`(workoutPersistenceMock.load(any<File>())).thenReturn(Single.error(IllegalAccessException()))

        // when
        val state = runSutOn(defaultWorkoutId, defaultState)
                .skip(1)
                .map { it.showError().get() }
                .test()

        // then
        state.assertValue("the default error message")
    }

    @Test
    fun `When store fails to provide workout, sut completes`() {
        // given
        `when`(workoutPersistenceMock.load(any<File>())).thenReturn(Single.error(IllegalAccessException("the error message")))

        // when
        val observer = runSutOn(defaultWorkoutId, defaultState).test()

        // then
        observer.assertComplete()
    }

}