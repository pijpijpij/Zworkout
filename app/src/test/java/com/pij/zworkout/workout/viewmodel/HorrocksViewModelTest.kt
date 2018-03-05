package com.pij.zworkout.workout.viewmodel

import com.annimon.stream.Optional
import com.pij.horrocks.DefaultEngine
import com.pij.horrocks.Result
import com.pij.horrocks.SysoutLogger
import com.pij.zworkout.workout.Model
import io.reactivex.Observable
import org.junit.Before
import org.junit.Ignore
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
    private lateinit var loadingFeatureMock: io.reactivex.functions.Function<String, Observable<Result<Model>>>
    @Mock
    private lateinit var createWorkoutFeatureMock: io.reactivex.functions.Function<Any, Result<Model>>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        sut = HorrocksViewModel.create(SysoutLogger(), DefaultEngine<Model, Model>(SysoutLogger()),
                loadingFeatureMock,
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
        observer.assertValue(Model.create(false, Optional.empty(), ""))
    }

    @Ignore("not implemented yet")
    @Test
    fun `Loading triggers LoadingFeature`() {
        // given
        `when`(loadingFeatureMock.apply("an id")).thenReturn(Observable.never())
        sut.model().test()

        // when
        sut.load("an id")

        // then
        verify(loadingFeatureMock).apply("an id")
    }

    @Ignore("not implemented yet")
    @Test
    fun `model() returns model provided by LoadingFeature on load()`() {
        // given
        val loaded = Model.create(true, Optional.empty(), "")
        `when`(loadingFeatureMock.apply(any())).thenReturn(Observable.just(Result { _ -> loaded }))
        val observer = sut.model().skip(1).test()

        // when
        sut.load("an id")

        // then
        observer.assertValue(loaded)
    }

    @Test
    fun `model() returns model provided by CreateDetailFeature on createWorkout()`() {
        // given
        val dummyModel = Model.create(true, Optional.empty(), "")
        `when`(createWorkoutFeatureMock.apply(any())).thenReturn(Result { _ -> dummyModel })
        val observer = sut.model().skip(1).test()

        // when
        sut.createWorkout()

        // then
        observer.assertValue(dummyModel)
    }

}
