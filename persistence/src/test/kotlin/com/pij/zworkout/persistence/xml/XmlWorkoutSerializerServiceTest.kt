package com.pij.zworkout.persistence.xml

import com.nhaarman.mockitokotlin2.mock
import com.pij.zworkout.persistence.api.PersistableWorkout
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.simpleframework.xml.core.Persister
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 *
 * Created on 24/03/2018.
 *
 * @author Pierrejean
 */
@RunWith(Enclosed::class)
class XmlWorkoutSerializerServiceTest {

    class SerializationTests {

        private lateinit var persisterMock: Persister
        private lateinit var sut: XmlWorkoutSerializerService

        @BeforeTest
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
        fun `write() does not emit error if persister does not throw`() {
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
            `when`(persisterMock.write(any(), any<OutputStream>())).thenThrow(IllegalArgumentException("who ha!"))

            // when
            val result = sut.write(data, output).test()

            // then
            result.assertErrorMessage("who ha!")
        }

    }

    class DeserializationTests {

        private lateinit var persisterMock: Persister
        private lateinit var sut: XmlWorkoutSerializerService

        @BeforeTest
        fun setUp() {
            persisterMock = mock()
            sut = XmlWorkoutSerializerService(persisterMock)

        }

        @Test
        fun `read() without subscribing does not throw`() {
            // given

            // when
            sut.read(ByteArrayInputStream(byteArrayOf()))

            // then
        }

        @Test
        fun `Subscribing to read() does not throw`() {
            // given

            // when
            sut.read(ByteArrayInputStream(byteArrayOf())).test()

            // then
        }

        @Test
        fun `read() calls the persister`() {
            // given
            val input = ByteArrayInputStream(byteArrayOf())

            // when
            sut.read(input).test()

            // then
            verify(persisterMock).read(any<PersistableWorkout>(), any<InputStream>())
        }

        @Test
        fun `read() does not emit error if persister does not throw`() {
            // given
            val input = ByteArrayInputStream(byteArrayOf())
            `when`(persisterMock.read(any<PersistableWorkout>(), any<InputStream>())).thenReturn(PersistableWorkout())

            // when
            val result = sut.read(input).test()

            // then
            result.assertNoErrors()
        }

        @Test
        fun `read() emits error if persister throws`() {
            // given
            val input = ByteArrayInputStream(byteArrayOf())
            `when`(persisterMock.read(any<PersistableWorkout>(), any<InputStream>())).thenThrow(IllegalArgumentException("who ha!"))

            // when
            val result = sut.read(input).test()

            // then
            result.assertErrorMessage("who ha!")
        }

    }
}