package com.pij.zworkout.service.android

import com.annimon.stream.Optional
import com.pij.utils.SysoutLogger
import com.pij.zworkout.service.api.WorkoutFile
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.arrayWithSize
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import java.net.URI
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.expect

/**
 *
 * Created on 02/03/2018.
 *
 * @author Pierrejean
 */
class FolderStorageServiceTest {

    @Rule
    @JvmField
    val folderManager = TemporaryFolder()

    private lateinit var sut: FolderStorageService

    @BeforeTest
    fun setUp() {
        sut = FolderStorageService(folderManager.root, SysoutLogger())
    }

    private fun create(uri: URI) = WorkoutFile.UNDEFINED.toBuilder().uri(Optional.of(uri)).build()

    private fun createWorkoutFile(filename: String): WorkoutFile {
        val file = folderManager.newFile(filename)
        val dummyFile = create(file.toURI())
        file.delete()
        return dummyFile
    }

    @Test
    fun `workouts() emits empty when the root folder is empty`() {
        // given

        //when
        val observer = sut.workouts().test()

        // then
        observer.assertValueCount(1)
                .assertValue { it.isEmpty() }
    }

    @Test
    fun `workouts() emits empty when only non-Zwift files are in the root folder`() {
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
    fun `workouts() emits a list of 2 when there are 2 Zwift files in the root folder in spite of case issues in file extension`() {
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
    fun `workouts() emits a list a single file if there's a single Zwift file in the root folder`() {
        // given
        folderManager.newFile("file1.zwo")

        //when
        val observer = sut.workouts().test()

        // then
        observer.assertValue { it.size == 1 }
    }

    @Test
    fun `workouts() emits a list with the single Zwift file in the root folder`() {
        // given
        val file1 = folderManager.newFile("file1.zwo")

        //when
        val files = sut.workouts()
                .flatMapIterable { list -> list }
                .test()

        // then
        val result = files.values()[0]
        expect(file1.toURI()) { result.uri().get() }
        expect("file1") { result.name() }
    }

    @Test
    fun `workouts() emits a list with all 3 Zwift files in the root folder`() {
        // given
        val file1 = folderManager.newFile("file1.zwo")
        val file2 = folderManager.newFile("file2.zwo")
        val file3 = folderManager.newFile("file3.zwo")

        //when
        val files = sut.workouts()
                .flatMapIterable { list -> list }
                .test()

        // then
        assertThat(files.values().map(WorkoutFile::uri).map(Optional<URI>::get), containsInAnyOrder(file1.toURI(), file2.toURI(), file3.toURI()))
        assertThat(files.values().map(WorkoutFile::name), containsInAnyOrder("file1", "file2", "file3"))
    }


    @Test
    fun `calling open() without subscribing to it does not throw`() {
        // given

        // when
        sut.openForWrite(WorkoutFile.UNDEFINED)

        // then
    }

    @Test
    fun `open() an undefined File does not fail`() {
        // given
        val file = WorkoutFile.UNDEFINED

        // when
        val stream = sut.openForWrite(file).test()

        // then
        stream.assertNoErrors()
    }

    @Test
    fun `open() an undefined file creates it`() {
        // given
        val file = WorkoutFile.UNDEFINED

        // when
        sut.openForWrite(file).test()

        // then
        val expectedFile = folderManager.root.list { _, name -> name == ".zwo" }
        assertThat(expectedFile, arrayWithSize(1))
    }

    @Test
    fun `open() a non existent File works`() {
        // given
        val file = WorkoutFile.UNDEFINED.toBuilder().name("hello").build()

        // when
        val stream = sut.openForWrite(file).test()

        // then
        stream.assertNoErrors()
    }

    @Test
    fun `open() a non-existent file creates it`() {
        // given
        val file = WorkoutFile.UNDEFINED.toBuilder().name("hello").build()

        // when
        sut.openForWrite(file).test()

        // then
        val expectedFile = folderManager.root.list { _, name -> name == "hello.zwo" }
        assertThat(expectedFile, arrayWithSize(1))
    }

    @Test
    fun `open() a non-absolute URI errors`() {
        // given
        val file = create(URI.create("somestuff"))

        // when
        val stream = sut.openForWrite(file).test()

        // then
        stream.assertError(IllegalArgumentException::class.java)
    }

    @Test
    fun `open() an absolute non-file URI errors`() {
        // given
        val file = create(URI.create("http://somestuff"))

        // when
        val stream = sut.openForWrite(file).test()

        // then
        stream.assertError(IllegalArgumentException::class.java)
    }

    @Test
    fun `open() a file URI does not error`() {
        // given
        val file = createWorkoutFile("afile.zwo")

        // when
        val stream = sut.openForWrite(file).test()

        // then
        stream.assertNoErrors()
    }

}
