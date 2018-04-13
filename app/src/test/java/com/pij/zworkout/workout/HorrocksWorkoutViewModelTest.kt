/*
 * Copyright (c) 2018, Chiswick Forest
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

package com.pij.zworkout.workout

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.pij.horrocks.*
import com.pij.utils.SysoutLogger
import com.pij.zworkout.uc.Workout
import com.pij.zworkout.workout.feature.InsertEffortFeature.Companion.END_OF_LIST
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

    private val simpleModel = Model(true, null, false, "", false, "")
    private val simpleState = State(true, null, false, Workout(), false, null)

    /** Provides {@link #simpleState} when called.
     */
    private val simpleResult = Reducer<State> { _ -> simpleState }


    private lateinit var sut: HorrocksWorkoutViewModel

    private lateinit var loadingFeatureMock: AsyncInteraction<String, State>
    private lateinit var createWorkoutFeatureMock: Interaction<Any, State>
    private lateinit var nameFeatureMock: Interaction<String, State>
    private lateinit var descriptionFeatureMock: Interaction<String, State>
    private lateinit var saveFeatureMock: AsyncInteraction<Any, State>
    private lateinit var insertEffortFeatureMock: Interaction<Pair<ModelEffort, Int>, State>
    private lateinit var setEffortFeatureMock: Interaction<Pair<ModelEffort, Int>, State>
    private lateinit var editEffortPropertyFeatureMock: Interaction<EffortPropertyEvent, State>


    @BeforeTest
    fun setUp() {
        loadingFeatureMock = mock()
        createWorkoutFeatureMock = mock()
        nameFeatureMock = mock()
        descriptionFeatureMock = mock()
        saveFeatureMock = mock()
        insertEffortFeatureMock = mock()
        setEffortFeatureMock = mock()
        editEffortPropertyFeatureMock = mock()
        sut = HorrocksWorkoutViewModel.create(SysoutLogger(), DefaultEngine<State, Model>(SysoutLogger()),
                MemoryStorage(HorrocksWorkoutViewModel.initialState()),
                WorkoutStateConverter(StateEffortConverter()),
                nameFeatureMock,
                descriptionFeatureMock,
                loadingFeatureMock,
                saveFeatureMock,
                insertEffortFeatureMock,
                setEffortFeatureMock,
                editEffortPropertyFeatureMock,
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
        observer.assertValue(Model(false, null, false, "", false, ""))
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

    @Test
    fun `addEffort() triggers insertion of an effort in the model`() {
        // given
        sut.model().test()

        // when
        sut.addEffort()

        // then
        verify(insertEffortFeatureMock).process(any())
    }

    @Test
    fun `addEffort() triggers insertion of SteadyState 120-Z1 in the model`() {
        // given
        sut.model().test()

        // when
        sut.addEffort()

        // then
        verify(insertEffortFeatureMock).process(Pair(ModelSteadyState(120, "Z1"), END_OF_LIST))
    }

    @Test
    fun `model() returns model provided by insertEffortFeature on addEffort()`() {
        // given
        `when`(insertEffortFeatureMock.process(any())).thenReturn(Reducer { _ -> simpleState })
        val observer = sut.model().skip(1).test()

        // when
        sut.addEffort()

        // then
        observer.assertValue(simpleModel)
    }

    @Test
    fun `setEffort() triggers the set feature`() {
        // given
        val effort = ModelSteadyState(120, "Z1")
        sut.model().test()

        // when
        sut.setEffort(effort, 23)

        // then
        verify(setEffortFeatureMock).process(any<Pair<ModelEffort, Int>>())
    }

    @Test
    fun `model() returns model emitted by SetEffortFeature on setEffort()`() {
        // given
        val effort = ModelSteadyState(120, "Z1")
        whenever(setEffortFeatureMock.process(any())).thenReturn(Reducer { _ -> simpleState })
        val observer = sut.model().skip(1).test()

        // when
        sut.setEffort(effort, 23)

        // then
        observer.assertValue(simpleModel)
    }

    @Test
    fun `editEffortProperty() triggers the edit effort property feature`() {
        // given
        val effort = SteadyStatePowerEvent(123, "Z1")
        sut.model().test()

        // when
        sut.editEffortProperty(effort)

        // then
        verify(editEffortPropertyFeatureMock).process(any<EffortPropertyEvent>())
    }

    @Test
    fun `model() returns model emitted by EditEffortPropertyFeature on setEffort()`() {
        // given
        val effort = SteadyStatePowerEvent(123, "Z1")
        whenever(editEffortPropertyFeatureMock.process(any())).thenReturn(Reducer { _ -> simpleState })
        val observer = sut.model().skip(1).test()

        // when
        sut.editEffortProperty(effort)

        // then
        observer.assertValue(simpleModel)
    }

}
