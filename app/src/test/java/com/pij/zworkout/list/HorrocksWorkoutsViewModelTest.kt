package com.pij.zworkout.list

import com.nhaarman.mockitokotlin2.mock
import com.pij.horrocks.AsyncInteraction
import com.pij.horrocks.DefaultEngine
import com.pij.horrocks.Interaction
import com.pij.horrocks.Reducer
import com.pij.utils.SysoutLogger
import io.reactivex.Observable
import junit.framework.TestCase.assertFalse
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.hasSize
import org.mockito.Mockito.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNull

/**
 *
 * Created on 01/03/2018.
 *
 * @author Pierrejean
 */
class HorrocksWorkoutsViewModelTest {

    private lateinit var sut: HorrocksWorkoutsViewModel

    private lateinit var loadingFeatureMock: AsyncInteraction<Any, Model>
    private lateinit var showDetailFeatureMock: Interaction<WorkoutInfo, Model>
    private lateinit var createWorkoutFeatureMock: Interaction<Any, Model>

    @BeforeTest
    fun setUp() {
        loadingFeatureMock = mock()
        showDetailFeatureMock = mock()
        createWorkoutFeatureMock = mock()
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
        assertFalse(initial.inProgress)
        assertNull(initial.showWorkout)
        assertThat(initial.workouts, hasSize(0))
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
        val workout = WorkoutInfo("1", "the one", "who cares")
        val loaded = Model(true, null, null, false, listOf(workout))
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
        val failedLoad = Model(false, "the error", null, false, emptyList())
        `when`(loadingFeatureMock.process(any())).thenReturn(Observable.just(Reducer { _ -> failedLoad }))
        `when`(createWorkoutFeatureMock.process(any())).thenReturn(Reducer { it })
        val states = sut.model().skip(1).test()

        // when
        sut.load()
        sut.createWorkout()

        // then
        assertThat(states.values().map { it.showError }, contains("the error", null))
    }

    @Test
    fun `selecting a workout triggers ShowDetailFeature`() {
        // given
        val workout = WorkoutInfo("1", "the one", "who cares")
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
        val workout = WorkoutInfo("1", "the one", "who cares")
        val dummyModel = Model(true, null, null, false, listOf(workout))
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
        val workout = WorkoutInfo("1", "the one", "who cares")
        val showDetail = Model(false, null, workout, false, listOf(workout))
        `when`(showDetailFeatureMock.process(any())).thenReturn(Reducer { _ -> showDetail })
        `when`(loadingFeatureMock.process(any())).thenReturn(Observable.just(Reducer { it }))
        val states = sut.model().skip(1).test()

        // when
        sut.select(workout)
        sut.load()

        // then
        assertThat(states.values().map { it.showWorkout }, contains(workout, null))
    }

    @Test
    fun `model() returns model provided by CreateDetailFeature on createWorkout()`() {
        // given
        val dummyModel = Model(true, null, null, false, emptyList())
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
        val createWorkoutModel = Model(false, null, null, true, emptyList())
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
