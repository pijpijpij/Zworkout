package com.pij.zworkout.list.viewmodel

import com.annimon.stream.Optional
import com.pij.horrocks.DefaultEngine
import com.pij.horrocks.Result
import com.pij.horrocks.SysoutLogger
import com.pij.zworkout.list.Model
import com.pij.zworkout.list.WorkoutInfo
import io.reactivex.Observable
import junit.framework.TestCase.assertFalse
import org.hamcrest.MatcherAssert.assertThat
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
    private lateinit var showDetailFeatureMock: io.reactivex.functions.Function<WorkoutInfo, Result<Model>>
    @Mock
    private lateinit var createWorkoutFeatureMock: io.reactivex.functions.Function<Any, Result<Model>>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        sut = HorrocksViewModel.create(SysoutLogger(), DefaultEngine<Model, Model>(SysoutLogger()),
                loadingFeatureMock,
                showDetailFeatureMock,
                createWorkoutFeatureMock)
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
        val workout = WorkoutInfo.create("1", "the one", Optional.of("who cares"))
        val loaded = Model.create(true, Optional.empty(), false, Optional.empty(), listOf(workout))
        `when`(loadingFeatureMock.apply(any())).thenReturn(Observable.just(Result { _ -> loaded }))
        val observer = sut.model().skip(1).test()

        // when
        sut.load()

        // then
        observer.assertValue(loaded)
    }

    @Test
    fun `selecting a workout triggers ShowDetailFeature`() {
        // given
        val workout = WorkoutInfo.create("1", "the one", Optional.of("who cares"))
        `when`(showDetailFeatureMock.apply(any())).thenReturn(Result { it })
        sut.model().test()
        reset(showDetailFeatureMock)

        // when
        sut.select(workout)

        // then
        verify(showDetailFeatureMock).apply(any())
    }

    @Test
    fun `model() returns model provided by ShowDetailFeature on select()`() {
        // given
        val workout = WorkoutInfo.create("1", "the one", Optional.of("who cares"))
        val dummyModel = Model.create(true, Optional.empty(), false, Optional.empty(), listOf(workout))
        `when`(showDetailFeatureMock.apply(any())).thenReturn(Result { _ -> dummyModel })
        val observer = sut.model().skip(1).test()

        // when
        sut.select(workout)

        // then
        observer.assertValue(dummyModel)
    }

    @Test
    fun `showDetail is reset when unrelated model is emitted`() {
        // given
        val workout = WorkoutInfo.create("1", "the one", Optional.of("who cares"))
        val showDetail = Model.create(false, Optional.of(workout), false, Optional.empty(), listOf(workout))
        `when`(showDetailFeatureMock.apply(any())).thenReturn(Result { _ -> showDetail })
        `when`(loadingFeatureMock.apply(any())).thenReturn(Observable.just(Result { it }))
        val observer = sut.model().skip(1).map(Model::showWorkout).test()

        // when
        sut.select(workout)
        sut.load()

        // then
        observer.assertValues(Optional.of(workout), Optional.empty())
    }

    @Test
    fun `model() returns model provided by CreateDetailFeature on createWorkout()`() {
        // given
        val dummyModel = Model.create(true, Optional.empty(), false, Optional.empty(), emptyList())
        `when`(createWorkoutFeatureMock.apply(any())).thenReturn(Result { _ -> dummyModel })
        val observer = sut.model().skip(1).test()

        // when
        sut.createWorkout()

        // then
        observer.assertValue(dummyModel)
    }

    @Test
    fun `createWorkout is reset when unrelated model is emitted`() {
        // given
        val createWorkoutModel = Model.create(false, Optional.empty(), true, Optional.empty(), emptyList())
        `when`(createWorkoutFeatureMock.apply(any())).thenReturn(Result { _ -> createWorkoutModel })
        `when`(loadingFeatureMock.apply(any())).thenReturn(Observable.just(Result { it }))
        val observer = sut.model().skip(1).map(Model::createWorkout).test()

        // when
        sut.createWorkout()
        sut.load()

        // then
        observer.assertValues(true, false)
    }

}
