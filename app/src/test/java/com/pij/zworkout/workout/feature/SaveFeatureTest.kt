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

package com.pij.zworkout.workout.feature

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.pij.horrocks.StateProvider
import com.pij.utils.SysoutLogger
import com.pij.zworkout.list.WorkoutInfo
import com.pij.zworkout.service.api.WorkoutFile
import com.pij.zworkout.uc.Workout
import com.pij.zworkout.uc.WorkoutPersistenceUC
import com.pij.zworkout.workout.State
import io.reactivex.Single
import org.junit.Assume.assumeFalse
import org.junit.Assume.assumeTrue
import org.mockito.Mockito.`when`
import java.io.File
import java.net.URI
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * Created on 02/01/2018.
 * @author PierreJean
 */
class SaveFeatureTest {

    private lateinit var storageMock: WorkoutPersistenceUC
    private lateinit var stateProviderMock: StateProvider<State>

    private val defaultState = State(false, null, null, false, Workout(), false, null)

    private lateinit var sut: SaveFeature
    private lateinit var workoutFile: WorkoutFile
    private lateinit var workoutInfo: WorkoutInfo

    @BeforeTest
    fun setUp() {
        storageMock = mock()
        stateProviderMock = mock()
        `when`(storageMock.save(any(), any())).thenReturn(Single.never())
        `when`(stateProviderMock.get()).thenReturn(defaultState)
        workoutFile = WorkoutFile(URI.create("some/file"), "zip")
        workoutInfo = WorkoutInfo("some/file", "zip", "zap")
        sut = SaveFeature(SysoutLogger(), storageMock, stateProviderMock, "the default error message")
    }

    private fun runSutOn(state: State) = sut.process(Any()).map { it.reduce(state) }

    @Test
    fun `Before storage service saves the workout, sut emits Start`() {
        // given

        // when
        val states = runSutOn(defaultState)
                .map { it.inProgress }
                .test()

        // then
        states.assertValue(true)
    }


    @Test
    fun `SaveFeature delegates to storage`() {
        // given

        // when
        runSutOn(defaultState).test()

        // then
        verify(storageMock).save(defaultState.workout, defaultState.file)
    }

    @Test
    fun `When storage succeeds, sut emits not in progress`() {
        // given
        `when`(storageMock.save(any(), anyOrNull())).thenReturn(Single.just(File("")))

        // when
        val states = runSutOn(defaultState)
                .skip(1)
                .map { it.inProgress }
                .test()

        // then
        states.assertValue(false)
    }

    @Test
    fun `When storage succeeds, sut emits showSaved`() {
        // given
        `when`(storageMock.save(any(), anyOrNull())).thenReturn(Single.just(File("")))

        // when
        val states = runSutOn(defaultState)
                .skip(1)
                .map { it.showSaved }
                .test()

        // then
        states.assertValue(true)
    }

    @Test
    fun `When storage succeeds, sut emits updated file`() {
        // given
        `when`(storageMock.save(any(), anyOrNull())).thenReturn(Single.just(File("")))
        assumeTrue(defaultState.file == null)

        // when
        val states = runSutOn(defaultState)
                .skip(1)
                .test()

        // then
        states.assertValueCount(1)
    }

    @Test
    fun `When storage succeeds, sut emits name read-only`() {
        // given
        `when`(storageMock.save(any(), anyOrNull())).thenReturn(Single.just(File("")))
        assumeFalse(defaultState.nameIsReadOnly)

        // when
        val states = runSutOn(defaultState)
                .skip(1)
                .test()

        states.assertValue { it.nameIsReadOnly }
    }


    @Test
    fun `When storage succeeds, sut completes`() {
        // given
        `when`(storageMock.save(any(), anyOrNull())).thenReturn(Single.just(File("")))

        // when
        val observer = sut.process(Any())
                .test()

        // then
        observer.assertComplete()
    }

    @Test
    fun `When store fails, sut emits not in progress`() {
        // given
        `when`(storageMock.save(any(), anyOrNull())).thenReturn(Single.error(IllegalAccessException("the error message")))

        // when
        val states = runSutOn(defaultState)
                .skip(1)
                .test()

        // then
        states.assertValue { !it.inProgress }
    }

    @Test
    fun `When store fails with a message, sut emits Failure with the exception message`() {
        // given
        `when`(storageMock.save(any(), anyOrNull())).thenReturn(Single.error(IllegalAccessException("the error message")))

        // when
        val states = runSutOn(defaultState)
                .skip(1)
                .map { it.showError }
                .test()

        // then
        states.assertValue("the error message")
    }

    @Test
    fun `When store fails with an empty message, sut emits Failure with the default message`() {
        // given
        `when`(storageMock.save(any(), anyOrNull())).thenReturn(Single.error(IllegalAccessException()))

        // when
        val states = runSutOn(defaultState)
                .skip(1)
                .map { it.showError }
                .test()

        // then
        states.assertValue("the default error message")
    }

    @Test
    fun `When store fails, sut completes`() {
        // given
        `when`(storageMock.save(any(), anyOrNull())).thenReturn(Single.error(IllegalAccessException("the error message")))

        // when
        val states = sut.process(Any())
                .test()

        // then
        states.assertComplete()
    }

}