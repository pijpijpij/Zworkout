package com.pij.zworkout.workout.feature

import com.annimon.stream.Optional
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.pij.horrocks.StateProvider
import com.pij.utils.SysoutLogger
import com.pij.zworkout.list.WorkoutInfo
import com.pij.zworkout.service.api.WorkoutFile
import com.pij.zworkout.uc.Workout
import com.pij.zworkout.uc.WorkoutPersistenceUC
import com.pij.zworkout.workout.State
import io.reactivex.Single
import org.junit.Assume.assumeFalse
import org.junit.Assume.assumeTrue
import org.mockito.Mockito.`when`
import java.io.File
import java.net.URI
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * Created on 02/01/2018.
 * @author PierreJean
 */
class SaveFeatureTest {

    private lateinit var storageMock: WorkoutPersistenceUC
    private lateinit var stateProviderMock: StateProvider<State>

    private val defaultState = State.create(false, Optional.empty(), false, Workout.EMPTY, false, Optional.empty())

    private lateinit var sut: SaveFeature
    private lateinit var workoutFile: WorkoutFile
    private lateinit var workoutInfo: WorkoutInfo

    @BeforeTest
    fun setUp() {
        storageMock = mock()
        stateProviderMock = mock()
        `when`(storageMock.save(any(), any())).thenReturn(Single.never())
        `when`(stateProviderMock.get()).thenReturn(defaultState)
        workoutFile = WorkoutFile.create(URI.create("some/file"), "zip")
        workoutInfo = WorkoutInfo.create("some/file", "zip", Optional.of("zap"))
        sut = SaveFeature(SysoutLogger(), storageMock, stateProviderMock, "the default error message")
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
        `when`(storageMock.save(any(), any())).thenReturn(Single.just(File("")))

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
        `when`(storageMock.save(any(), any())).thenReturn(Single.just(File("")))

        // when
        val states = sut.process(Any())
                .map { result -> result.reduce(defaultState) }
                .skip(1)
                .test()

        // then
        states.assertValue { it.showSaved() }
    }

    @Test
    fun `When storage succeeds, sut emits updated file`() {
        // given
        `when`(storageMock.save(any(), any())).thenReturn(Single.just(File("")))
        assumeFalse(defaultState.file().isPresent)

        // when
        val states = sut.process(Any())
                .map { result -> result.reduce(defaultState) }
                .map { it.file() }
                .skip(1)
                .test()

        // then
        states.assertValue { it.isPresent }
    }

    @Test
    fun `When storage succeeds, sut emits name read-only`() {
        // given
        `when`(storageMock.save(any(), any())).thenReturn(Single.just(File("")))
        assumeTrue(!defaultState.nameIsReadOnly())

        // when
        val states = sut.process(Any())
                .map { result -> result.reduce(defaultState) }
                .skip(1)
                .test()

        states.assertValue { it.nameIsReadOnly() }
    }


    @Test
    fun `When storage succeeds, sut completes`() {
        // given
        `when`(storageMock.save(any(), any())).thenReturn(Single.just(File("")))

        // when
        val observer = sut.process(Any())
                .test()

        // then
        observer.assertComplete()
    }

    @Test
    fun `When store fails, sut emits not in progress`() {
        // given
        `when`(storageMock.save(any(), any())).thenReturn(Single.error(IllegalAccessException("the error message")))

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
        `when`(storageMock.save(any(), any())).thenReturn(Single.error(IllegalAccessException("the error message")))

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
        `when`(storageMock.save(any(), any())).thenReturn(Single.error(IllegalAccessException()))

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
        `when`(storageMock.save(any(), any())).thenReturn(Single.error(IllegalAccessException("the error message")))

        // when
        val states = sut.process(Any())
                .test()

        // then
        states.assertComplete()
    }

}