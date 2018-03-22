package com.pij.zworkout.persistence.api

import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import org.simpleframework.xml.core.Persister
import org.simpleframework.xml.core.ValueRequiredException
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.expect


/**
 *
 * Created on 13/03/2018.
 *
 * @author Pierrejean
 */
@RunWith(Enclosed::class)
class PersistableWorkoutTest {

    abstract class BaseSerializationTest {

        private val serializer: Persister = Persister()

        fun serialize(xml: String): PersistableWorkout = serializer.read(PersistableWorkout::class.java, xml)

    }

    @RunWith(Parameterized::class)
    class ReadingInvalidXmlTests(private val expected: KClass<Throwable>, private val xml: String) : BaseSerializationTest() {

        @Test
        fun test() {
            // given, when and then :)
            assertFailsWith(expected) { serialize(xml) }
        }

        companion object {
            @Parameters(name = "test {index}: {1} is invalid")
            @JvmStatic
            fun data(): Collection<Array<Any>> {
                return listOf(
                        arrayOf(ValueRequiredException::class, "<workout_file/>")
                )
            }
        }
    }

    @RunWith(Parameterized::class)
    class ReadingValidXmlTests(private val xml: String) : BaseSerializationTest() {

        @Test
        fun test() {
            // when
            serialize(xml)
        }

        companion object {
            @Parameters(name = "test {index}: {0} is valid")
            @JvmStatic
            fun data(): Collection<Array<Any>> {
                return listOf(
                        arrayOf<Any>("<workout_file><name/></workout_file>"),
                        arrayOf<Any>("<workout_file><name></name></workout_file>")
                )
            }
        }
    }


    class ReadingAdHocTests : BaseSerializationTest() {

        @Test
        fun `Reading with a space in the name loads the space`() {
            // given
            val xml = "<workout_file><name> </name></workout_file>"

            // when
            val workout = serialize(xml)

            // then
            expect(" ") { workout.name.value }
        }

        @Test
        fun `Reading a non-blank name loads it`() {
            // given
            val xml = "<workout_file><name>asdf</name></workout_file>"

            // when
            val workout = serialize(xml)

            // then
            expect("asdf") { workout.name.value }
        }
    }
}
