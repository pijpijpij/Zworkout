package com.pij.zworkout.list.feature

import com.nhaarman.mockitokotlin2.mock
import com.pij.utils.SysoutLogger
import com.pij.zworkout.list.Model
import com.pij.zworkout.list.WorkoutInfo
import com.pij.zworkout.service.api.StorageService
import com.pij.zworkout.service.api.WorkoutFile
import io.reactivex.Observable
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsInAnyOrder
import org.mockito.Mockito.`when`
import java.net.URI
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 *
 * Created on 02/01/2018.
 *
 * @author PierreJean
 */
class StorageLoadingFeatureTest {

    private lateinit var storageServiceMock: StorageService

    private val defaultState = Model(false, null, null, false, emptyList())

    private lateinit var sut: StorageLoadingFeature
    private lateinit var workoutFile: WorkoutFile
    private lateinit var workoutInfo: WorkoutInfo

    @BeforeTest
    fun setUp() {
        storageServiceMock = mock()
        `when`(storageServiceMock.workouts()).thenReturn(Observable.never())
        workoutFile = WorkoutFile(URI("some/file"), "zip")
        workoutInfo = WorkoutInfo("some/file", "zip", null)
        sut = StorageLoadingFeature(SysoutLogger(), storageServiceMock, "the default error message")
    }

    @Test
    fun `Before store provides workouts, sut emits Start`() {
        // given

        // when
        val states = sut.process(Any()).map { it.reduce(defaultState) }
                .map { it.inProgress }
                .test()

        // then
        states.assertValue(true)
    }

    @Test
    fun `When store succeeds, sut emits not inProgress`() {
        // given
        `when`(storageServiceMock.workouts()).thenReturn(Observable.just(listOf(workoutFile)))

        // when
        val states = sut.process(Any()).map { it.reduce(defaultState) }
                .skip(1)
                .map { it.inProgress }
                .test()

        // then
        states.assertValue(false)
    }

    @Test
    fun `When store succeeds, sut emits a list of workout`() {
        // given
        `when`(storageServiceMock.workouts()).thenReturn(Observable.just(listOf(workoutFile)))

        // when
        val states = sut.process(Any()).map { it.reduce(defaultState) }
                .skip(1)
                .map { it.workouts }
                .test()

        // then
        states.assertValueCount(1)
    }

    @Test
    fun `When store succeeds, sut emits a workoutInfo matching that workout store provided`() {
        // given
        `when`(storageServiceMock.workouts()).thenReturn(Observable.just(listOf(workoutFile)))

        // when
        val states = sut.process(Any()).map { it.reduce(defaultState) }
                .skip(1)
                .map { it.workouts }
                .flatMapIterable { it }
                .test()

        // then
        assertThat(states.values(), containsInAnyOrder(workoutInfo))
    }

    @Test
    fun `When store succeeds, sut completes`() {
        // given
        `when`(storageServiceMock.workouts()).thenReturn(Observable.just(listOf(workoutFile)))

        // when
        val observer = sut.process(Any()).map { it.reduce(defaultState) }.test()

        // then
        observer.assertComplete()
    }

    @Test
    fun `When store fails with a message, sut emits one item`() {
        // given
        `when`(storageServiceMock.workouts()).thenReturn(Observable.error(IllegalAccessException("the error message")))

        // when
        val observer = sut.process(Any()).map { it.reduce(defaultState) }
                .skip(1)
                .test()

        // then
        observer.assertValueCount(1)
    }

    @Test
    fun `When store fails, sut emits not in progress`() {
        // given
        `when`(storageServiceMock.workouts()).thenReturn(Observable.error(IllegalAccessException("the error message")))

        // when
        val observer = sut.process(Any()).map { it.reduce(defaultState) }
                .skip(1)
                .map { it.inProgress }
                .test()

        // then
        observer.assertValue(false)
    }

    @Test
    fun `When store fails with a message, sut emits the exception message`() {
        // given
        `when`(storageServiceMock.workouts()).thenReturn(Observable.error(IllegalAccessException("the error message")))

        // when
        val observer = sut.process(Any()).map { it.reduce(defaultState) }
                .skip(1)
                .map { it.showError }
                .test()

        // then
        observer.assertValue("the error message")
    }

    @Test
    fun `When store fails with an empty message, sut emits Failure with the default message`() {
        // given
        `when`(storageServiceMock.workouts()).thenReturn(Observable.error(IllegalAccessException()))

        // when
        val observer = sut.process(Any()).map { it.reduce(defaultState) }
                .skip(1)
                .map { it.showError }
                .test()

        // then
        observer.assertValue("the default error message")
    }

    @Test
    fun `When store fails, sut completes`() {
        // given
        `when`(storageServiceMock.workouts()).thenReturn(Observable.error(IllegalAccessException("the error message")))

        // when
        val observer = sut.process(Any()).map { it.reduce(defaultState) }
                .test()

        // then
        observer.assertComplete()
    }

}