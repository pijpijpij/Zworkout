package com.pij.zworkout.persistence.xml

import com.nhaarman.mockitokotlin2.mock
import com.pij.zworkout.persistence.api.PersistableWorkout
import org.junit.Before
import org.mockito.Mockito.*
import org.simpleframework.xml.core.Persister
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import kotlin.test.Test

/**
 *
 * Created on 24/03/2018.
 *
 * @author Pierrejean
 */
class XmlWorkoutSerializerServiceTest {

    private lateinit var persisterMock: Persister
    private lateinit var sut: XmlWorkoutSerializerService

    @Before
    fun setUp() {
        persisterMock = mock()
        sut = XmlWorkoutSerializerService(persisterMock)

    }

    @Test
    fun `write() without subscribing does not throw`() {
        // given

        // when
        sut.write(PersistableWorkout(), ByteArrayOutputStream())

        // then
    }

    @Test
    fun `Subscribing to write() does not throw`() {
        // given

        // when
        sut.write(PersistableWorkout(), ByteArrayOutputStream()).test()

        // then
    }

    @Test
    fun `write() calls the persister`() {
        // given
        val output = ByteArrayOutputStream()
        val data = PersistableWorkout()

        // when
        sut.write(data, output).test()

        // then
        verify(persisterMock).write(data, output)
    }

    @Test
    fun `write() does not fail if persister does not`() {
        // given
        val output = ByteArrayOutputStream()
        val data = PersistableWorkout()

        // when
        val result = sut.write(data, output).test()

        // then
        result.assertNoErrors()
    }

    @Test
    fun `write() emits error if persister throws`() {
        // given
        val output = ByteArrayOutputStream()
        val data = PersistableWorkout()
        `when`(persisterMock.write(any(), any(OutputStream::class.java))).thenThrow(IllegalArgumentException("who ha!"))

        // when
        val result = sut.write(data, output).test()

        // then
        result.assertErrorMessage("who ha!")
    }

}