package com.pij.zworkout.service.android

import com.annimon.stream.Stream
import com.pij.zworkout.service.api.WorkoutFile
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsInAnyOrder
import org.hamcrest.Matchers.equalTo
import org.junit.Rule
import org.junit.rules.TemporaryFolder
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

    @BeforeTest
    fun setUp() {
        sut = FolderStorageService(folderManager.root)
    }

    private fun create(uri: URI) = WorkoutFile.UNDEFINED.toBuilder().uri(uri).build()

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
        assertThat(result.uri(), equalTo(file1.toURI()))
        assertThat(result.name(), equalTo("file1"))
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
        assertThat(Stream.of(files.values()).map(WorkoutFile::uri).toList(), containsInAnyOrder(file1.toURI(), file2.toURI(), file3.toURI()))
        assertThat(Stream.of(files.values()).map(WorkoutFile::name).toList(), containsInAnyOrder("file1", "file2", "file3"))
    }


    @Test
    fun `calling open() without subscribing to it does not throw`() {
        // given

        // when
        sut.open(WorkoutFile.UNDEFINED)

        // then
    }

    @Test
    fun `open() an undefined File errors`() {
        // given
        val file = WorkoutFile.UNDEFINED

        // when
        val stream = sut.open(file).test()

        // then
        stream.assertError(IllegalArgumentException::class.java)
    }

    @Test
    fun `open() a non-absolute URI errors`() {
        // given
        val file = create(URI.create("somestuff"))

        // when
        val stream = sut.open(file).test()

        // then
        stream.assertError(IllegalArgumentException::class.java)
    }

    @Test
    fun `open() an absolute non-file URI errors`() {
        // given
        val file = create(URI.create("http://somestuff"))

        // when
        val stream = sut.open(file).test()

        // then
        stream.assertError(IllegalArgumentException::class.java)
    }

    @Test
    fun `open() a file URI does not error`() {
        // given
        val file = createWorkoutFile("afile.zwo")

        // when
        val stream = sut.open(file).test()

        // then
        stream.assertNoErrors()
    }

}
