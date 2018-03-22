package com.pij.zworkout.uc

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.pij.zworkout.persistence.api.PersistableWorkoutTestUtil
import com.pij.zworkout.persistence.api.WorkoutSerializerService
import com.pij.zworkout.service.api.StorageService
import com.pij.zworkout.service.api.WorkoutFile
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.mockito.Mockito.`when`
import java.io.ByteArrayOutputStream
import java.net.URI
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 *
 * Created on 02/03/2018.
 *
 * @author Pierrejean
 */
class DefaultWorkoutPersistenceUCTest {

    private lateinit var sut: DefaultWorkoutPersistenceUC

    private lateinit var storageMock: StorageService
    private lateinit var serializerMock: WorkoutSerializerService

    private val dummyFile = WorkoutFile.UNDEFINED

    private lateinit var converterMock: PersistableWorkoutConverter

    @BeforeTest
    fun setUp() {
        storageMock = mock()
        serializerMock = mock()
        converterMock = mock()
        `when`(storageMock.workouts()).thenReturn(Observable.never())
        `when`(converterMock.convert(any())).thenReturn(PersistableWorkoutTestUtil.empty())
        `when`(storageMock.open(any())).thenReturn(Single.never())
        sut = DefaultWorkoutPersistenceUC(storageMock, serializerMock, converterMock)
    }

    private fun create(uri: String) = WorkoutFile.UNDEFINED.toBuilder().uri(URI.create(uri)).build()

    @Test
    fun `workouts() does not throw`() {
        // given

        //when
        sut.workouts()

        // then
    }

    @Test
    fun `workouts() calls storage service`() {
        // given

        //when
        sut.workouts().test()

        // then
        verify(storageMock).workouts()
    }

    @Test
    fun `workouts() emits 2 items when storage service emits 2 items`() {
        // given
        `when`(storageMock.workouts()).thenReturn(Observable.just(listOf(create("file1"), create("file2"))))

        //when
        val list = sut.workouts().test()

        // then
        list.assertValueCount(1)
    }

    @Test
    fun `workouts() emits 2 items emitted by storage service`() {
        // given
        `when`(storageMock.workouts()).thenReturn(Observable.just(listOf(create("file1"), create("file2"))))

        //when
        val items = sut.workouts().flatMapIterable { it }
                .test()

        // then
        items.assertValues(create("file1"), create("file2"))
    }

    @Test
    fun `workouts() fails when storage service fails`() {
        // given
        `when`(storageMock.workouts()).thenReturn(Observable.error(IllegalStateException()))

        //when
        val list = sut.workouts().test()

        // then
        list.assertError(IllegalStateException::class.java)
    }


    @Test
    fun `save() does not throw`() {
        // given

        // when
        sut.save(Workout.EMPTY, dummyFile)

        // then
    }

    @Test
    fun `save() calls the storage service`() {
        // given

        // when
        sut.save(Workout.EMPTY, dummyFile).test()

        // then
        verify(storageMock).open(dummyFile)
    }

    @Test
    fun `save() calls the workout serializer`() {
        // given
        `when`(storageMock.open(dummyFile)).thenReturn(Single.just(ByteArrayOutputStream()))

        // when
        sut.save(Workout.EMPTY, dummyFile).test()

        // then
        verify(serializerMock).write(any(), any())
    }

    @Test
    fun `save() succeeds when the workout serializer succeeds`() {
        // given
        `when`(storageMock.open(dummyFile)).thenReturn(Single.just(ByteArrayOutputStream()))
        `when`(serializerMock.write(any(), any())).thenReturn(Completable.complete())

        // when
        val result = sut.save(Workout.EMPTY, dummyFile).test()

        // then
        result.assertComplete()
    }

    @Test
    fun `save() fails when the storage service fails`() {
        // given
        `when`(storageMock.open(dummyFile)).thenReturn(Single.error(IllegalStateException()))

        // when
        val result = sut.save(Workout.EMPTY, dummyFile).test()

        // then
        result.assertError { true }
    }

    @Test
    fun `save() fails when the workout serializer fails`() {
        // given
        `when`(storageMock.open(dummyFile)).thenReturn(Single.just(ByteArrayOutputStream()))
        `when`(serializerMock.write(any(), any())).thenReturn(Completable.error(IllegalStateException()))

        // when
        val result = sut.save(Workout.EMPTY, dummyFile).test()

        // then
        result.assertError { true }
    }
}
