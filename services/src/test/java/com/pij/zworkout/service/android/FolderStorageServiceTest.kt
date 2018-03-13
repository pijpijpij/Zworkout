package com.pij.zworkout.service.android

import com.annimon.stream.Stream
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.pij.zworkout.service.api.Workout
import com.pij.zworkout.service.api.WorkoutFile
import com.pij.zworkout.service.api.WorkoutSerializerService
import io.reactivex.Completable
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsInAnyOrder
import org.hamcrest.Matchers.equalTo
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.mockito.Mockito.`when`
import java.net.URI
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 *
 * Created on 02/03/2018.
 *
 * @author Pierrejean
 */
class FolderStorageServiceTest {

    @Rule
    @kotlin.jvm.JvmField
    public val folderManager = TemporaryFolder()

    private lateinit var sut: FolderStorageService

    private lateinit var serializerMock: WorkoutSerializerService

    @BeforeTest
    fun setUp() {
        serializerMock = mock()
        sut = FolderStorageService(folderManager.root, serializerMock)
    }

    private fun createWorkoutFile(filename: String): WorkoutFile {
        val file = folderManager.newFile(filename)
        val dummyFile = create(file.toURI())
        file.delete()
        return dummyFile
    }

    @Test
    fun `load() emits empty when the root folder is empty`() {
        // given

        //when
        val observer = sut.workouts().test()

        // then
        observer.assertValueCount(1)
                .assertValue { it.isEmpty() }
    }

    @Test
    fun `load() emits empty when only non-Zwift files are in the root folder`() {
        // given
        folderManager.newFile("file1")
        folderManager.newFile("file2.jpg")

        //when
        val observer = sut.workouts().test()

        // then
        observer.assertValueCount(1)
                .assertValue { it.isEmpty() }
    }

    @Test
    fun `load() emits a list of 2 when there are 2 Zwift files in the root folder in spite of case issues in file extension`() {
        // given
        folderManager.newFile("file1.Zwo")
        folderManager.newFile("file2.ZWO")

        //when
        val observer = sut.workouts().test()

        // then
        observer.assertValueCount(1)
                .assertValue { it.size == 2 }
    }

    @Test
    fun `load() emits a list a single file if there's a single Zwift file in the root folder`() {
        // given
        folderManager.newFile("file1.zwo")

        //when
        val observer = sut.workouts().test()

        // then
        observer.assertValue { it.size == 1 }
    }

    @Test
    fun `load() emits a list with the single Zwift file in the root folder`() {
        // given
        val file1 = folderManager.newFile("file1.zwo")

        //when
        val files = sut.workouts()
                .flatMapIterable { list -> list }
                .test()

        // then
        val result = files.values()[0]
        assertThat(result.uri(), equalTo(file1.toURI()))
        assertThat(result.name(), equalTo("file1"))
    }

    @Test
    fun `load() emits a list with all 3 Zwift files in the root folder`() {
        // given
        val file1 = folderManager.newFile("file1.zwo")
        val file2 = folderManager.newFile("file2.zwo")
        val file3 = folderManager.newFile("file3.zwo")

        //when
        val files = sut.workouts()
                .flatMapIterable { list -> list }
                .test()

        // then
        assertThat(Stream.of(files.values()).map(WorkoutFile::uri).toList(), containsInAnyOrder(file1.toURI(), file2.toURI(), file3.toURI()))
        assertThat(Stream.of(files.values()).map(WorkoutFile::name).toList(), containsInAnyOrder("file1", "file2", "file3"))
    }


    @Test
    fun `save does not throw`() {
        // given

        // when
        sut.save(Workout.EMPTY, WorkoutFile.UNDEFINED)

        // then
    }

    @Test
    fun `save() onto an undefined File errors`() {
        // given

        // when
        val result = sut.save(Workout.EMPTY, WorkoutFile.UNDEFINED).test()

        // then
        result.assertError(IllegalArgumentException::class.java)
    }

    @Test
    fun `save() onto a non-absolute URI errors`() {
        // given
        val file: WorkoutFile = create(URI.create("somestuff"))

        // when
        val result = sut.save(Workout.EMPTY, file).test()

        // then
        result.assertError(IllegalArgumentException::class.java)
    }

    @Test
    fun `save() onto a absolute non-file URI errors`() {
        // given
        val file: WorkoutFile = create(URI.create("http://somestuff"))

        // when
        val result = sut.save(Workout.EMPTY, file).test()

        // then
        result.assertError(IllegalArgumentException::class.java)
    }

    private fun create(uri: URI) = WorkoutFile.UNDEFINED.toBuilder().uri(uri).build()

    @Test
    fun `save() onto a absolute file URI does not error`() {
        // given
        val dummyFile = createWorkoutFile("afile.zwo")
        `when`(serializerMock.write(any(), any())).thenReturn(Completable.never())

        // when
        val result = sut.save(Workout.EMPTY, dummyFile).test()

        // then
        result.assertNoErrors()
    }

    @Test
    fun `save() ont a file URI calls the workout serializer`() {
        // given
        val dummyFile = createWorkoutFile("dummyFile")

        // when
        sut.save(Workout.EMPTY, dummyFile).test()

        // then
        verify(serializerMock).write(eq(Workout.EMPTY), any())
    }

    @Test
    fun `save() succeeds when the workout serializer succeeds`() {
        // given
        val dummyFile = createWorkoutFile("dummyFile")
        `when`(serializerMock.write(any(), any())).thenReturn(Completable.complete())

        // when
        val result = sut.save(Workout.EMPTY, dummyFile).test()

        // then
        result.assertComplete()
    }

    @Test
    fun `save() fails when the workout serializer fails`() {
        // given
        `when`(serializerMock.write(any(), any())).thenReturn(Completable.error(IllegalStateException()))

        // when
        val result = sut.save(Workout.EMPTY, WorkoutFile.UNDEFINED).test()

        // then
        result.assertError { true }
    }
}
