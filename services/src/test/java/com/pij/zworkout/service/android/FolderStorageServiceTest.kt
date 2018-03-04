package com.pij.zworkout.service.android

import com.annimon.stream.Stream
import com.pij.zworkout.service.api.WorkoutFile
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

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

    @Before
    fun setUp() {
        sut = FolderStorageService(folderManager.root)
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
    fun `load() list Zwift files in the root folder in spite of case issues in file extension`() {
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
    fun `load() lists the single Zwift file in the root folder`() {
        // given
        val file1 = folderManager.newFile("file1.zwo")

        //when
        val observer = sut.workouts().test()

        // then
        observer.assertValue { it.size == 1 }
        val result = observer.values()[0][0]
        assertThat(result.uri(), equalTo(file1.toURI()))
        assertThat(result.name(), equalTo("file1"))
    }

    @Test
    fun `load() lists all Zwift files in the root folder`() {
        // given
        val file1 = folderManager.newFile("file1.zwo")
        val file2 = folderManager.newFile("file2.zwo")
        val file3 = folderManager.newFile("file3.zwo")

        //when
        val observer = sut.workouts().test()

        // then
        val result = observer.values()[0]
        assertThat(result, hasSize(3))
        assertThat(Stream.of(result).map(WorkoutFile::uri).toList(), containsInAnyOrder(file1.toURI(), file2.toURI(), file3.toURI()))
        assertThat(Stream.of(result).map(WorkoutFile::name).toList(), containsInAnyOrder("file1", "file2", "file3"))
    }
}