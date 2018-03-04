package com.pij.zworkout.list.viewmodel

import com.annimon.stream.Optional
import com.pij.horrocks.SysoutLogger
import com.pij.zworkout.list.Model
import com.pij.zworkout.list.WorkoutDescriptor
import com.pij.zworkout.service.api.StorageService
import com.pij.zworkout.service.api.WorkoutFile
import io.reactivex.Observable
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.net.URI

/**
 *
 * Created on 02/01/2018.
 *
 * @author PierreJean
 */
class StorageLoadingFeatureTest {

    @Mock
    private lateinit var storageServiceMock: StorageService

    private val defaultState = Model.create(false, Optional.empty(), Optional.empty(), emptyList())

    private lateinit var sut: StorageLoadingFeature
    private lateinit var workoutFile: WorkoutFile
    private lateinit var workoutDescriptor: WorkoutDescriptor

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        `when`(storageServiceMock.workouts()).thenReturn(Observable.never())
        workoutFile = WorkoutFile.create(URI.create("some/file"), "zip", Optional.of("zap"))
        workoutDescriptor = WorkoutDescriptor.create("some/file", "zip", Optional.of("zap"))
        sut = StorageLoadingFeature(SysoutLogger(), storageServiceMock, "the default error message")
    }

    @Test
    fun `Emits Start result before storage succeeds`() {
        // given

        // when
        val states = sut.apply(Any()).map({ result -> result.applyTo(defaultState) }).test()

        // then
        states.assertValue({ state -> state.inProgress() })
    }

    @Test
    fun `Emits Success result when storage succeeds`() {
        // given
        `when`(storageServiceMock.workouts()).thenReturn(Observable.just(listOf(workoutFile)))

        // when
        val observer = sut.apply(Any()).map({ result -> result.applyTo(defaultState) }).test()

        // then
        val actual = observer.values()[1]
        assertFalse(actual.inProgress())
        assertThat(actual.workouts(), containsInAnyOrder(workoutDescriptor))
    }

    @Test
    fun `Completes when storage succeeds`() {
        // given
        `when`(storageServiceMock.workouts()).thenReturn(Observable.just(listOf(workoutFile)))

        // when
        val observer = sut.apply(Any()).map({ result -> result.applyTo(defaultState) }).test()

        // then
        observer.assertComplete()
    }

    @Test
    fun `Emits Failure result when storage fails with a message`() {
        // given
        `when`(storageServiceMock.workouts()).thenReturn(Observable.error(IllegalAccessException("the error message")))

        // when
        val observer = sut.apply(Any()).map({ result -> result.applyTo(defaultState) }).test()

        // then
        val actual = observer.values()[1]
        assertFalse(actual.inProgress())
        assertEquals(actual.showError().get(), "the error message")
    }

    @Test
    fun `Emits Failure result when storage fails with empty message`() {
        // given
        `when`(storageServiceMock.workouts()).thenReturn(Observable.error(IllegalAccessException()))

        // when
        val observer = sut.apply(Any()).map({ result -> result.applyTo(defaultState) }).test()

        // then
        val actual = observer.values()[1]
        assertFalse(actual.inProgress())
        assertEquals(actual.showError().get(), "the default error message")
    }

    @Test
    fun `Completes when storage fails`() {
        // given
        `when`(storageServiceMock.workouts()).thenReturn(Observable.error(IllegalAccessException("the error message")))

        // when
        val observer = sut.apply(Any()).map({ result -> result.applyTo(defaultState) }).test()

        // then
        observer.assertComplete()
    }

}