package com.pij.zworkout.service.android

import com.pij.utils.SysoutLogger
import com.pij.zworkout.service.api.WorkoutFile
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.Assume.assumeTrue
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import java.io.File
import java.io.FileNotFoundException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

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
        assertEquals(file1.toURI(), result.uri)
        assertEquals("file1", result.name )
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
        assertThat(files.values().map(WorkoutFile::uri), containsInAnyOrder(file1.toURI(), file2.toURI(), file3.toURI()))
        assertThat(files.values().map(WorkoutFile::name), containsInAnyOrder("file1", "file2", "file3"))
    }


    @Test
    fun `Calling create() without subscribing to it does not throw`() {
        // given

        // when
        sut.create("")

        // then
    }

    @Test
    fun `create() with an empty name fails`() {
        // given

        // when
        val result = sut.create("").test()

        // then
        result.assertError(IllegalArgumentException::class.java)
        result.assertError { it -> it.message!!.contains("already exists") }
    }

    @Test
    fun `create() with a valid name does not fail`() {
        // given

        // when
        val result = sut.create("zipzap").test()

        // then
        result.assertNoErrors()
    }

    @Test
    fun `create() with a valid name on an existing file emits error`() {
        // given
        val file = folderManager.newFile("file1")
        assumeTrue(file.exists())

        // when
        val result = sut.create("file1").test()

        // then
        result.assertError(IllegalArgumentException::class.java)
    }

    @Test
    fun `create() with a valid name on a new file succeeds`() {
        // given

        // when
        val file = sut.create("file1").test()

        // then
        file.assertComplete()
    }

    @Test
    fun `create() with a valid name on a new file does not create the file`() {
        // given

        // when
        val file = sut.create("file1").test()

        // then
        file.assertValue { f -> !f.exists() }
    }


    @Test
    fun `Calling openForWrite() without subscribing to it does not throw`() {
        // given

        // when
        sut.openForWrite(File(""))

        // then
    }

    @Test
    fun `openForWrite() an undefined file fails`() {
        // given

        // when
        val stream = sut.openForWrite(File("")).test()

        // then
        stream.assertError(IllegalArgumentException::class.java)
    }

    @Test
    fun `openForWrite() a relative file fails`() {
        // given

        // when
        val stream = sut.openForWrite(File("some name")).test()

        // then
        stream.assertError(IllegalArgumentException::class.java)
    }

    @Test
    fun `openForWrite() a file outside of the root fails`() {
        // given
        val file = File("some name").absoluteFile

        // when
        val stream = sut.openForWrite(file).test()

        // then
        stream.assertError(IllegalArgumentException::class.java)
    }

    @Test
    fun `openForWrite() a non-existent file in the root does not fail`() {
        // given
        val file = File(folderManager.root, "hello")

        // when
        val stream = sut.openForWrite(file).test()

        // then
        stream.assertNoErrors()
    }

    @Test
    fun `openForWrite() an existing file in the root does not fail`() {
        // given
        val file = folderManager.newFile()

        // when
        val stream = sut.openForWrite(file).test()

        // then
        stream.assertNoErrors()
    }


    @Test
    fun `Calling openForRead() without subscribing to it does not throw`() {
        // given

        // when
        sut.openForRead(File(""))

        // then
    }

    @Test
    fun `openForRead() an undefined file fails`() {
        // given

        // when
        val stream = sut.openForRead(File("")).test()

        // then
        stream.assertError(IllegalArgumentException::class.java)
    }

    @Test
    fun `openForRead() a relative file fails`() {
        // given

        // when
        val stream = sut.openForRead(File("some name")).test()

        // then
        stream.assertError(IllegalArgumentException::class.java)
    }

    @Test
    fun `openForRead() a file outside of the root fails`() {
        // given
        val file = File("some name").absoluteFile

        // when
        val stream = sut.openForRead(file).test()

        // then
        stream.assertError(IllegalArgumentException::class.java)
    }

    @Test
    fun `openForRead() a non-existent file in the root does not fail`() {
        // given
        val file = File(folderManager.root, "hello")

        // when
        val stream = sut.openForRead(file).test()

        // then
        stream.assertError(FileNotFoundException::class.java)
    }

    @Test
    fun `openForRead() an existing file in the root does not fail`() {
        // given
        val file = folderManager.newFile()

        // when
        val stream = sut.openForRead(file).test()

        // then
        stream.assertNoErrors()
    }

}
