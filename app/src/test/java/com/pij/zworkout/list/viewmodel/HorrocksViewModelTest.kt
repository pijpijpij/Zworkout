package com.pij.zworkout.list.viewmodel

import com.pij.horrocks.DefaultEngine
import com.pij.horrocks.Result
import com.pij.horrocks.SysoutLogger
import com.pij.zworkout.list.Model
import com.pij.zworkout.list.Model.create
import com.pij.zworkout.list.WorkoutDescriptor
import io.reactivex.Observable
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.hasSize
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.*

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

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        sut = HorrocksViewModel.create(SysoutLogger(), DefaultEngine<Model, Model>(SysoutLogger()), loadingFeatureMock)
    }

    @Test
    fun `initial model subscription does not fail`() {
        // given

        // when
        val observer = sut.model().test()

        // then
        observer.assertNoErrors()
    }

    @Test
    fun `initial model subscription receives 1 model`() {
        // given

        // when
        val observer = sut.model().test()

        // then
        observer.assertValueCount(1)
    }

    @Test
    fun `initial model subscription receives empty model`() {
        // given

        // when
        val observer = sut.model().test()

        // then
        val result = observer.values()[0]
        assertFalse(result.inProgress())
        assertThat(result.workouts(), hasSize(0))
    }

    @Test
    fun `load() triggers LoadingFeature`() {
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
        val workout = WorkoutDescriptor.builder()
                .name("the name")
                .id("the id")
                .details("some details")
                .build()
        val loaded = create(true, Collections.singletonList(workout))
        `when`(loadingFeatureMock.apply(any())).thenReturn(Observable.just(Result<Model> { _ -> loaded }))
        val observer = sut.model().test()

        // when
        sut.load()

        // then
        val result = observer.values()[1]
        assertTrue(result.inProgress())
        assertThat(result.workouts(), Matchers.contains(workout))
    }
}