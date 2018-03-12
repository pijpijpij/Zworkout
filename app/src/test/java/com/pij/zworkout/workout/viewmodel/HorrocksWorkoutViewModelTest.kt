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
class HorrocksWorkoutViewModelTest {

    private val simpleModel = Model.create(true, Optional.empty(), "")

    /** Provides {@link #simpleModel} when called.
     */
    private val simpleResult = Result<Model> { _ -> simpleModel }


    private lateinit var sut: HorrocksWorkoutViewModel

    @Mock
    private lateinit var loadingFeatureMock: io.reactivex.functions.Function<String, Observable<Result<Model>>>
    @Mock
    private lateinit var createWorkoutFeatureMock: io.reactivex.functions.Function<Any, Result<Model>>
    @Mock
    private lateinit var nameFeatureMock: io.reactivex.functions.Function<String, Result<Model>>
    @Mock
    private lateinit var saveFeatureMock: io.reactivex.functions.Function<Any, Observable<Result<Model>>>


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        sut = HorrocksWorkoutViewModel.create(SysoutLogger(), DefaultEngine<Model, Model>(SysoutLogger()),
                nameFeatureMock,
                loadingFeatureMock,
                saveFeatureMock,
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

    @Test
    fun `Changing name triggers NameFeature`() {
        // given
        `when`(nameFeatureMock.apply("a name")).thenReturn(simpleResult)
        sut.model().test()

        // when
        sut.name("a name")

        // then
        verify(nameFeatureMock).apply("a name")
    }

    @Test
    fun `model() returns model provided by ChangeNameFeature on setName()`() {
        // given
        `when`(nameFeatureMock.apply(anyString())).thenReturn(Result { _ -> simpleModel })
        val observer = sut.model().skip(1).test()

        // when
        sut.name("whatever")

        // then
        observer.assertValue(simpleModel)
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
        `when`(loadingFeatureMock.apply(any())).thenReturn(Observable.just(simpleResult))
        val observer = sut.model().skip(1).test()

        // when
        sut.load("an id")

        // then
        observer.assertValue(simpleModel)
    }

    @Test
    fun `Creating Workout triggers CreateWorkoutFeature`() {
        // given
        `when`(createWorkoutFeatureMock.apply(any())).thenReturn(simpleResult)
        sut.model().test()

        // when
        sut.createWorkout()

        // then
        verify(createWorkoutFeatureMock).apply(any())
    }

    @Test
    fun `model() returns model provided by CreateDetailFeature on createWorkout()`() {
        // given
        `when`(createWorkoutFeatureMock.apply(any())).thenReturn(Result { _ -> simpleModel })
        val observer = sut.model().skip(1).test()

        // when
        sut.createWorkout()

        // then
        observer.assertValue(simpleModel)
    }

    @Test
    fun `save() triggers save of the model`() {
        // given
        sut.model().test()

        // when
        sut.save()

        // then
        verify(saveFeatureMock).apply(any())
    }

    @Test
    fun `model() returns model provided by SaveFeature on save()`() {
        // given
        `when`(saveFeatureMock.apply(any())).thenReturn(Observable.just(Result { _ -> simpleModel }))
        val observer = sut.model().skip(1).test()

        // when
        sut.save()

        // then
        observer.assertValue(simpleModel)
    }

}
