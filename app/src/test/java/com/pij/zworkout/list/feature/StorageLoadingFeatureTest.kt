package com.pij.zworkout.list.feature

import com.annimon.stream.Optional
import com.nhaarman.mockitokotlin2.mock
import com.pij.utils.SysoutLogger
import com.pij.zworkout.list.Model
import com.pij.zworkout.list.WorkoutInfo
import com.pij.zworkout.service.api.StorageService
import com.pij.zworkout.service.api.WorkoutFile
import io.reactivex.Observable
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
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

    private val defaultState = Model.create(false, Optional.empty(), Optional.empty(), false, emptyList())

    private lateinit var sut: StorageLoadingFeature
    private lateinit var workoutFile: WorkoutFile
    private lateinit var workoutInfo: WorkoutInfo

    @BeforeTest
    fun setUp() {
        storageServiceMock = mock()
        `when`(storageServiceMock.workouts()).thenReturn(Observable.never())
        workoutFile = WorkoutFile(URI.create("some/file"), "zip")
        workoutInfo = WorkoutInfo.create("some/file", "zip", Optional.empty())
        sut = StorageLoadingFeature(SysoutLogger(), storageServiceMock, "the default error message")
    }

    @Test
    fun `Before store provides workouts, sut emits Start`() {
        // given

        // when
        val states = sut.process(Any()).map({ result -> result.reduce(defaultState) }).test()

        // then
        states.assertValue { state -> state.inProgress() }
    }

    @Test
    fun `When store succeeds, sut emits Success`() {
        // given
        `when`(storageServiceMock.workouts()).thenReturn(Observable.just(listOf(workoutFile)))

        // when
        val observer = sut.process(Any()).map({ result -> result.reduce(defaultState) }).test()

        // then
        val actual = observer.values()[1]
        assertFalse(actual.inProgress())
        assertThat(actual.workouts(), containsInAnyOrder(workoutInfo))
    }

    @Test
    fun `When store succeeds, sut completes`() {
        // given
        `when`(storageServiceMock.workouts()).thenReturn(Observable.just(listOf(workoutFile)))

        // when
        val observer = sut.process(Any()).map({ result -> result.reduce(defaultState) }).test()

        // then
        observer.assertComplete()
    }

    @Test
    fun `When store fails with a message, sut emits Failure with the exception message`() {
        // given
        `when`(storageServiceMock.workouts()).thenReturn(Observable.error(IllegalAccessException("the error message")))

        // when
        val observer = sut.process(Any()).map({ result -> result.reduce(defaultState) }).test()

        // then
        val actual = observer.values()[1]
        assertFalse(actual.inProgress())
        assertEquals(actual.showError().get(), "the error message")
    }

    @Test
    fun `When store fails with an empty message, sut emits Failure with the default message`() {
        // given
        `when`(storageServiceMock.workouts()).thenReturn(Observable.error(IllegalAccessException()))

        // when
        val observer = sut.process(Any()).map({ result -> result.reduce(defaultState) }).test()

        // then
        val actual = observer.values()[1]
        assertFalse(actual.inProgress())
        assertEquals(actual.showError().get(), "the default error message")
    }

    @Test
    fun `When store fails, sut completes`() {
        // given
        `when`(storageServiceMock.workouts()).thenReturn(Observable.error(IllegalAccessException("the error message")))

        // when
        val observer = sut.process(Any()).map({ result -> result.reduce(defaultState) }).test()

        // then
        observer.assertComplete()
    }

}