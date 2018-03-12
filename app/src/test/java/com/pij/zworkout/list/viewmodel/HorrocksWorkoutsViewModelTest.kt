package com.pij.zworkout.list.viewmodel

import com.annimon.stream.Optional
import com.pij.horrocks.*
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
class HorrocksWorkoutsViewModelTest {

    private lateinit var sut: HorrocksWorkoutsViewModel

    @Mock
    private lateinit var loadingFeatureMock: AsyncInteraction<Any, Model>
    @Mock
    private lateinit var showDetailFeatureMock: Interaction<WorkoutInfo, Model>
    @Mock
    private lateinit var createWorkoutFeatureMock: Interaction<Any, Model>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        sut = HorrocksWorkoutsViewModel.create(SysoutLogger(), DefaultEngine<Model, Model>(SysoutLogger()),
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
        `when`(loadingFeatureMock.process(any())).thenReturn(Observable.never())
        sut.model().test()

        // when
        sut.load()

        // then
        verify(loadingFeatureMock).process(any())
    }

    @Test
    fun `model() returns model provided by LoadingFeature on load()`() {
        // given
        val workout = WorkoutInfo.create("1", "the one", Optional.of("who cares"))
        val loaded = Model.create(true, Optional.empty(), Optional.empty(), false, listOf(workout))
        `when`(loadingFeatureMock.process(any())).thenReturn(Observable.just(Reducer { _ -> loaded }))
        val observer = sut.model().skip(1).test()

        // when
        sut.load()

        // then
        observer.assertValue(loaded)
    }

    @Test
    fun `showError is reset when unrelated model is emitted`() {
        // given
        val failedLoad = Model.create(false, Optional.of("the error"), Optional.empty(), false, emptyList())
        `when`(loadingFeatureMock.process(any())).thenReturn(Observable.just(Reducer { _ -> failedLoad }))
        `when`(createWorkoutFeatureMock.process(any())).thenReturn(Reducer { it })
        val observer = sut.model().skip(1).map(Model::showError).test()

        // when
        sut.load()
        sut.createWorkout()

        // then
        observer.assertValues(Optional.of("the error"), Optional.empty())
    }

    @Test
    fun `selecting a workout triggers ShowDetailFeature`() {
        // given
        val workout = WorkoutInfo.create("1", "the one", Optional.of("who cares"))
        `when`(showDetailFeatureMock.process(any())).thenReturn(Reducer { it })
        sut.model().test()
        reset(showDetailFeatureMock)

        // when
        sut.select(workout)

        // then
        verify(showDetailFeatureMock).process(any())
    }

    @Test
    fun `model() returns model provided by ShowDetailFeature on select()`() {
        // given
        val workout = WorkoutInfo.create("1", "the one", Optional.of("who cares"))
        val dummyModel = Model.create(true, Optional.empty(), Optional.empty(), false, listOf(workout))
        `when`(showDetailFeatureMock.process(any())).thenReturn(Reducer { _ -> dummyModel })
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
        val showDetail = Model.create(false, Optional.empty(), Optional.of(workout), false, listOf(workout))
        `when`(showDetailFeatureMock.process(any())).thenReturn(Reducer { _ -> showDetail })
        `when`(loadingFeatureMock.process(any())).thenReturn(Observable.just(Reducer { it }))
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
        val dummyModel = Model.create(true, Optional.empty(), Optional.empty(), false, emptyList())
        `when`(createWorkoutFeatureMock.process(any())).thenReturn(Reducer { _ -> dummyModel })
        val observer = sut.model().skip(1).test()

        // when
        sut.createWorkout()

        // then
        observer.assertValue(dummyModel)
    }

    @Test
    fun `createWorkout is reset when unrelated model is emitted`() {
        // given
        val createWorkoutModel = Model.create(false, Optional.empty(), Optional.empty(), true, emptyList())
        `when`(createWorkoutFeatureMock.process(any())).thenReturn(Reducer { _ -> createWorkoutModel })
        `when`(loadingFeatureMock.process(any())).thenReturn(Observable.just(Reducer { it }))
        val observer = sut.model().skip(1).map(Model::createWorkout).test()

        // when
        sut.createWorkout()
        sut.load()

        // then
        observer.assertValues(true, false)
    }

}
