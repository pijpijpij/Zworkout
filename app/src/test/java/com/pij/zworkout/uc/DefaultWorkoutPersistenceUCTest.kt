package com.pij.zworkout.uc

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.pij.zworkout.persistence.api.PersistableWorkout
import com.pij.zworkout.persistence.api.PersistableWorkoutTestUtil
import com.pij.zworkout.persistence.api.WorkoutSerializerService
import com.pij.zworkout.service.api.StorageService
import com.pij.zworkout.service.api.WorkoutFile
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.mockito.Mockito.`when`
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
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
    private lateinit var converterMock: PersistableWorkoutConverter

    private val dummyFile = File("")


    @BeforeTest
    fun setUp() {
        storageMock = mock()
        serializerMock = mock()
        converterMock = mock()
        `when`(storageMock.workouts()).thenReturn(Observable.never())
        `when`(converterMock.convert(any<Workout>())).thenReturn(PersistableWorkoutTestUtil.empty())
        `when`(storageMock.create(any())).thenReturn(Single.never())
        `when`(storageMock.openForWrite(any())).thenReturn(Single.never())
        sut = DefaultWorkoutPersistenceUC(storageMock, serializerMock, converterMock)
    }

    private fun create(uri: String) = WorkoutFile(uri = URI.create(uri))

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
    fun `Calling save() without subscribing does not throw`() {
        // given

        // when
        sut.save(Workout(), dummyFile)

        // then
    }

    @Test
    fun `save() calls the storage service`() {
        // given

        // when
        sut.save(Workout(), dummyFile).test()

        // then
        verify(storageMock).openForWrite(dummyFile)
    }

    @Test
    fun `save() calls the workout serializer`() {
        // given
        `when`(storageMock.openForWrite(dummyFile)).thenReturn(Single.just(ByteArrayOutputStream()))

        // when
        sut.save(Workout(), dummyFile).test()

        // then
        verify(serializerMock).write(any(), any())
    }

    @Test
    fun `save() succeeds when the workout serializer succeeds`() {
        // given
        `when`(storageMock.openForWrite(dummyFile)).thenReturn(Single.just(ByteArrayOutputStream()))
        `when`(serializerMock.write(any(), any())).thenReturn(Completable.complete())

        // when
        val result = sut.save(Workout(), dummyFile).test()

        // then
        result.assertComplete()
    }

    @Test
    fun `save() fails when the storage service fails`() {
        // given
        `when`(storageMock.openForWrite(dummyFile)).thenReturn(Single.error(IllegalStateException()))

        // when
        val result = sut.save(Workout(), dummyFile).test()

        // then
        result.assertError { true }
    }

    @Test
    fun `save() fails when the workout serializer fails`() {
        // given
        `when`(storageMock.openForWrite(dummyFile)).thenReturn(Single.just(ByteArrayOutputStream()))
        `when`(serializerMock.write(any(), any())).thenReturn(Completable.error(IllegalStateException()))

        // when
        val result = sut.save(Workout(), dummyFile).test()

        // then
        result.assertError { true }
    }

    @Test
    fun `save() creates the file with the storage if no file is specified`() {
        // given
        val workout = Workout().copy(name = "hello!")

        // when
        sut.save(workout, null).test()

        // then
        verify(storageMock).create(any())
    }

    @Test
    fun `save() defines initial file name with a ZWO extension if no file is specified`() {
        // given
        val workout = Workout(name = "hello!")

        // when
        sut.save(workout, null).test()

        // then
        verify(storageMock).create("hello!.zwo")
    }

    @Test
    fun `save() opens the newly created file if no file is specified`() {
        // given
        `when`(storageMock.create("hello!.zwo")).thenReturn(Single.just(dummyFile))
        val workout = Workout(name = "hello!")

        // when
        sut.save(workout, null).test()

        // then
        verify(storageMock).openForWrite(dummyFile)
    }


    @Test
    fun `Calling load() without subscribing does not throw`() {
        // given

        // when
        sut.load(dummyFile)

        // then
    }

    @Test
    fun `load() calls the storage service`() {
        // given

        // when
        sut.load(dummyFile).test()

        // then
        verify(storageMock).openForRead(dummyFile)
    }

    @Test
    fun `load() uses the serializer Service to map the workout data`() {
        // given
        `when`(storageMock.openForRead(dummyFile)).thenReturn(Single.just(ByteArrayInputStream(byteArrayOf())))

        // when
        sut.load(dummyFile).test()

        // then
        verify(serializerMock).read(any())
    }

    @Test
    fun `load() uses the converter Service to map the workout data`() {
        // given
        `when`(storageMock.openForRead(dummyFile)).thenReturn(Single.just(ByteArrayInputStream(byteArrayOf())))
        `when`(serializerMock.read(any())).thenReturn(Single.just(PersistableWorkoutTestUtil.empty()))

        // when
        sut.load(dummyFile).test()

        // then
        verify(converterMock).convert(any<PersistableWorkout>())
    }

    @Test
    fun `load() provides a workout if storage, serializer and converter succeed`() {
        // given
        `when`(storageMock.openForRead(dummyFile)).thenReturn(Single.just(ByteArrayInputStream(byteArrayOf())))
        `when`(serializerMock.read(any())).thenReturn(Single.just(PersistableWorkoutTestUtil.empty()))
        `when`(converterMock.convert(any<PersistableWorkout>())).thenReturn(Workout())

        // when
        val workout = sut.load(dummyFile).test()

        // then
        workout.assertValueCount(1)
    }

}
