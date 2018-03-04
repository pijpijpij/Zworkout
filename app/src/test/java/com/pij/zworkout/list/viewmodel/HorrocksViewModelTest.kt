package com.pij.zworkout.list.viewmodel

import com.annimon.stream.Optional
import com.pij.horrocks.DefaultEngine
import com.pij.horrocks.Result
import com.pij.horrocks.SysoutLogger
import com.pij.zworkout.list.Model
import com.pij.zworkout.list.WorkoutDescriptor
import io.reactivex.Observable
import junit.framework.TestCase.assertFalse
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 *
 * Created on 01/03/2018.
 *
 * @author Pierrejean
 */
class HorrocksViewModelTest {

    private lateinit var sut: HorrocksViewModel

    @Mock
    private lateinit var loadingFeatureMock: io.reactivex.functions.Function<Any, Observable<Result<Model>>>
    @Mock
    private lateinit var showDetailFeatureMock: io.reactivex.functions.Function<WorkoutDescriptor, Result<Model>>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        sut = HorrocksViewModel.create(SysoutLogger(), DefaultEngine<Model, Model>(SysoutLogger()),
                loadingFeatureMock,
                showDetailFeatureMock)
    }

    @Test
    fun `Initial subscription to model() does not fail`() {
        // given

        // when
        val observer = sut.model().test()

        // then
        observer.assertNoErrors()
    }

    @Test
    fun `Initial subscription to model() receives 1 model`() {
        // given

        // when
        val observer = sut.model().test()

        // then
        observer.assertValueCount(1)
    }

    @Test
    fun `Initial subscription to model() receives empty model`() {
        // given

        // when
        val observer = sut.model().test()

        // then
        val initial = observer.values()[0]
        assertFalse(initial.inProgress())
        assertFalse(initial.showWorkout().isPresent)
        assertThat(initial.workouts(), hasSize(0))
    }

    @Test
    fun `Loading triggers LoadingFeature`() {
        // given
        `when`(loadingFeatureMock.apply(any())).thenReturn(Observable.never())
        sut.model().test()

        // when
        sut.load()

        // then
        verify(loadingFeatureMock).apply(any())
    }

    @Test
    fun `model() returns model provided by LoadingFeature on load()`() {
        // given
        val workout = WorkoutDescriptor.create("1", "the one", Optional.of("who cares"))
        val loaded = Model.create(true, Optional.empty(), Optional.empty(), listOf(workout))
        `when`(loadingFeatureMock.apply(any())).thenReturn(Observable.just(Result { _ -> loaded }))
        val observer = sut.model().test()

        // when
        sut.load()

        // then
        val next = observer.values()[1]
        assertThat(next, equalTo(loaded))
    }

    @Test
    fun `selecting a workout triggers ShowDetailFeature`() {
        // given
        val workout = WorkoutDescriptor.create("1", "the one", Optional.of("who cares"))
        `when`(showDetailFeatureMock.apply(any())).thenReturn(Result { it })
        sut.model().test()

        // when
        sut.select(workout)

        // then
        verify(showDetailFeatureMock).apply(any())
    }

    @Test
    fun `model() returns model provided by ShowDetailFeature on select()`() {
        // given
        val workout = WorkoutDescriptor.create("1", "the one", Optional.of("who cares"))
        val doNotShowDetail = Model.create(true, Optional.empty(), Optional.empty(), listOf(workout))
        `when`(showDetailFeatureMock.apply(any())).thenReturn(Result { _ -> doNotShowDetail })
        val observer = sut.model().test()

        // when
        sut.select(workout)

        // then
        val next = observer.values()[1]
        assertThat(next, equalTo(doNotShowDetail))
    }

    @Test
    fun `showDetail is reset when unrelated model is emitted`() {
        // given
        val workout = WorkoutDescriptor.create("1", "the one", Optional.of("who cares"))
        val showDetail = Model.create(true, Optional.of(workout), Optional.empty(), listOf(workout))
        `when`(showDetailFeatureMock.apply(any())).thenReturn(Result { _ -> showDetail })
        `when`(loadingFeatureMock.apply(any())).thenReturn(Observable.just(Result { it }))
        val observer = sut.model().test()

        // when
        sut.select(workout)
        sut.load()

        // then
        val next = observer.values()[2]
        assertFalse(next.showWorkout().isPresent)
    }
}