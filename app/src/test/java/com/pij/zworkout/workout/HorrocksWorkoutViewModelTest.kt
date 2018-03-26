package com.pij.zworkout.workout

import com.annimon.stream.Optional
import com.nhaarman.mockitokotlin2.mock
import com.pij.horrocks.*
import com.pij.utils.SysoutLogger
import com.pij.zworkout.uc.Workout
import io.reactivex.Observable
import org.mockito.Mockito.*
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 *
 * Created on 01/03/2018.
 *
 * @author Pierrejean
 */
class HorrocksWorkoutViewModelTest {

    private val simpleModel = Model.create(true, Optional.empty(), false, "", false, "")
    private val simpleState = State(true, null, false, Workout.EMPTY, false, null)

    /** Provides {@link #simpleState} when called.
     */
    private val simpleResult = Reducer<State> { _ -> simpleState }


    private lateinit var sut: HorrocksWorkoutViewModel

    private lateinit var loadingFeatureMock: AsyncInteraction<String, State>
    private lateinit var createWorkoutFeatureMock: Interaction<Any, State>
    private lateinit var nameFeatureMock: Interaction<String, State>
    private lateinit var descriptionFeatureMock: Interaction<String, State>
    private lateinit var saveFeatureMock: AsyncInteraction<Any, State>


    @BeforeTest
    fun setUp() {
        loadingFeatureMock = mock()
        createWorkoutFeatureMock = mock()
        nameFeatureMock = mock()
        descriptionFeatureMock = mock()
        saveFeatureMock = mock()
        sut = HorrocksWorkoutViewModel.create(SysoutLogger(), DefaultEngine<State, Model>(SysoutLogger()),
                MemoryStorage(HorrocksWorkoutViewModel.initialState()),
                nameFeatureMock,
                descriptionFeatureMock,
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
        observer.assertValue(Model.create(false, Optional.empty(), false, "", false, ""))
    }

    @Test
    fun `Changing name triggers NameFeature`() {
        // given
        `when`(nameFeatureMock.process("a name")).thenReturn(simpleResult)
        sut.model().test()

        // when
        sut.name("a name")

        // then
        verify(nameFeatureMock).process("a name")
    }

    @Test
    fun `model() returns model provided by ChangeNameFeature on setName()`() {
        // given
        `when`(nameFeatureMock.process(anyString())).thenReturn(Reducer { _ -> simpleState })
        val observer = sut.model().skip(1).test()

        // when
        sut.name("whatever")

        // then
        observer.assertValue(simpleModel)
    }

    @Test
    fun `Changing description triggers DescriptionFeature`() {
        // given
        `when`(descriptionFeatureMock.process("a description")).thenReturn(simpleResult)
        sut.model().test()

        // when
        sut.description("a description")

        // then
        verify(descriptionFeatureMock).process("a description")
    }

    @Test
    fun `model() returns model provided by ChangeDescriptionFeature on setDescription()`() {
        // given
        `when`(descriptionFeatureMock.process(anyString())).thenReturn(Reducer { _ -> simpleState })
        val observer = sut.model().skip(1).test()

        // when
        sut.description("whatever")

        // then
        observer.assertValue(simpleModel)
    }

    @Test
    fun `Loading triggers LoadingFeature`() {
        // given
        `when`(loadingFeatureMock.process("an id")).thenReturn(Observable.never())
        sut.model().test()

        // when
        sut.load("an id")

        // then
        verify(loadingFeatureMock).process("an id")
    }

    @Test
    fun `model() returns model provided by LoadingFeature on load()`() {
        // given
        `when`(loadingFeatureMock.process(any())).thenReturn(Observable.just(simpleResult))
        val observer = sut.model().skip(1).test()

        // when
        sut.load("an id")

        // then
        observer.assertValue(simpleModel)
    }

    @Test
    fun `Creating Workout triggers CreateWorkoutFeature`() {
        // given
        `when`(createWorkoutFeatureMock.process(any())).thenReturn(simpleResult)
        sut.model().test()

        // when
        sut.createWorkout()

        // then
        verify(createWorkoutFeatureMock).process(any())
    }

    @Test
    fun `model() returns model provided by CreateDetailFeature on createWorkout()`() {
        // given
        `when`(createWorkoutFeatureMock.process(any())).thenReturn(Reducer { _ -> simpleState })
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
        verify(saveFeatureMock).process(any())
    }

    @Test
    fun `model() returns model provided by SaveFeature on save()`() {
        // given
        `when`(saveFeatureMock.process(any())).thenReturn(Observable.just(Reducer { _ -> simpleState }))
        val observer = sut.model().skip(1).test()

        // when
        sut.save()

        // then
        observer.assertValue(simpleModel)
    }

}
