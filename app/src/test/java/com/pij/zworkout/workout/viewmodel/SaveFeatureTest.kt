package com.pij.zworkout.workout.viewmodel

import com.annimon.stream.Optional
import com.nhaarman.mockitokotlin2.mock
import com.pij.horrocks.StateProvider
import com.pij.horrocks.SysoutLogger
import com.pij.zworkout.list.WorkoutInfo
import com.pij.zworkout.service.api.StorageService
import com.pij.zworkout.service.api.Workout
import com.pij.zworkout.service.api.WorkoutFile
import com.pij.zworkout.service.api.WorkoutFileTestUtil
import com.pij.zworkout.workout.State
import io.reactivex.Completable
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import java.net.URI
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * Created on 02/01/2018.
 * @author PierreJean
 */
class SaveFeatureTest {

    private lateinit var storageServiceMock: StorageService
    private lateinit var stateProviderMock: StateProvider<State>

    private val defaultState = State.create(false, Optional.empty(), false, Workout.EMPTY, WorkoutFileTestUtil.empty())

    private lateinit var sut: SaveFeature
    private lateinit var workoutFile: WorkoutFile
    private lateinit var workoutInfo: WorkoutInfo

    @BeforeTest
    fun setUp() {
        storageServiceMock = mock()
        stateProviderMock = mock()
        `when`(storageServiceMock.save(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(Completable.never())
        `when`(stateProviderMock.get()).thenReturn(defaultState)
        workoutFile = WorkoutFile.create(URI.create("some/file"), "zip", Optional.of("zap"))
        workoutInfo = WorkoutInfo.create("some/file", "zip", Optional.of("zap"))
        sut = SaveFeature(SysoutLogger(), storageServiceMock, stateProviderMock, "the default error message")
    }

    @Test
    fun `Before storage service saves the workout, sut emits Start`() {
        // given

        // when
        val states = sut.process(Any())
                .map { result -> result.reduce(defaultState) }
                .test()

        // then
        states.assertValue { it.inProgress() }
    }

    @Test
    fun `When storage succeeds, sut emits not in progress`() {
        // given
        `when`(storageServiceMock.save(any(), any())).thenReturn(Completable.complete())

        // when
        val states = sut.process(Any())
                .map { result -> result.reduce(defaultState) }
                .skip(1)
                .test()

        // then
        states.assertValue { !it.inProgress() }
    }

    @Test
    fun `When storage succeeds, sut emits showSaved`() {
        // given
        `when`(storageServiceMock.save(any(), any())).thenReturn(Completable.complete())

        // when
        val states = sut.process(Any())
                .map { result -> result.reduce(defaultState) }
                .skip(1)
                .test()

        // then
        states.assertValue { it.showSaved() }
    }

    @Test
    fun `When storage succeeds, sut completes`() {
        // given
        `when`(storageServiceMock.save(any(), any())).thenReturn(Completable.complete())

        // when
        val observer = sut.process(Any())
                .test()

        // then
        observer.assertComplete()
    }

    @Test
    fun `When store fails, sut emits not in progress`() {
        // given
        `when`(storageServiceMock.save(any(), any())).thenReturn(Completable.error(IllegalAccessException("the error message")))

        // when
        val states = sut.process(Any())
                .map { result -> result.reduce(defaultState) }
                .skip(1)
                .test()

        // then
        states.assertValue { !it.inProgress() }
    }

    @Test
    fun `When store fails with a message, sut emits Failure with the exception message`() {
        // given
        `when`(storageServiceMock.save(any(), any())).thenReturn(Completable.error(IllegalAccessException("the error message")))

        // when
        val states = sut.process(Any())
                .map { result -> result.reduce(defaultState) }
                .skip(1)
                .test()

        // then
        states.assertValue { it.showError().get() == "the error message" }
    }

    @Test
    fun `When store fails with an empty message, sut emits Failure with the default message`() {
        // given
        `when`(storageServiceMock.save(any(), any())).thenReturn(Completable.error(IllegalAccessException()))

        // when
        val states = sut.process(Any())
                .map { result -> result.reduce(defaultState) }
                .skip(1)
                .test()

        // then
        states.assertValue { it.showError().get() == "the default error message" }
    }

    @Test
    fun `When store fails, sut completes`() {
        // given
        `when`(storageServiceMock.save(any(), any())).thenReturn(Completable.error(IllegalAccessException("the error message")))

        // when
        val states = sut.process(Any())
                .test()

        // then
        states.assertComplete()
    }

}