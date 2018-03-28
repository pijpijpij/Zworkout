/*
 * Copyright (c) 2018, Chiswick Forest
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

package com.pij.zworkout.persistence.xml

import com.pij.zworkout.persistence.api.*
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import org.simpleframework.xml.core.Persister
import org.simpleframework.xml.core.ValueRequiredException
import org.xmlunit.builder.DiffBuilder
import java.io.StringWriter
import kotlin.reflect.KClass
import kotlin.test.*


/**
 *
 * Created on 13/03/2018.
 *
 * @author Pierrejean
 */
@RunWith(Enclosed::class)
class PersistableWorkoutTest {

    abstract class BaseSerializationTest {

        private val serializer: Persister = workoutPersister()

        fun serialize(xml: String): PersistableWorkout = serializer.read(PersistableWorkout::class.java, xml)
        fun deserialize(bean: PersistableWorkout): String {
            val result = StringWriter()
            serializer.write(bean, result)
            return result.buffer.toString()
        }
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
                        arrayOf(ValueRequiredException::class, "<workout_file/>"),
                        arrayOf(IllegalArgumentException::class, "<workout_file><name/><sportType>not valid</sportType></workout_file>")
                )
            }
        }
    }

    @RunWith(Parameterized::class)
    class ReadingValidXmlTests(private val xml: String) : BaseSerializationTest() {

        @Ignore("Not coded yet")
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
                        arrayOf<Any>("<workout_file><name></name></workout_file>"),
                        arrayOf<Any>("<workout_file><name/><description/></workout_file>"),
                        arrayOf<Any>("<workout_file><name/><description> </description></workout_file>"),
                        arrayOf<Any>("<workout_file><name/><sportType/></workout_file>"),
                        arrayOf<Any>("<workout_file><name/><sportType>bike</sportType></workout_file>"),
                        arrayOf<Any>("<workout_file><name/><sportType/><workout/></workout_file>")
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
            assertEquals(" ", workout.name.value)
        }

        @Test
        fun `Reading a non-blank name loads it`() {
            // given
            val xml = "<workout_file><name>asdf</name></workout_file>"

            // when
            val workout = serialize(xml)

            // then
            assertEquals("asdf", workout.name.value)
        }

        @Test
        fun `Reading a missing sportType name loads it`() {
            // given
            val xml = "<workout_file><name>asdf</name></workout_file>"

            // when
            val workout = serialize(xml)

            // then
            assertEquals("asdf", workout.name.value)
        }
    }

    @RunWith(Parameterized::class)
    class WritingValidXmlTests(private val expected: String, private val bean: PersistableWorkout) : BaseSerializationTest() {

        @Test
        fun test() {
            // when
            val actual = deserialize(bean)

            val myDiff = DiffBuilder.compare(expected).withTest(actual)
                    .ignoreWhitespace()
                    .checkForSimilar()
                    .build()

            // then
            assertFalse(myDiff.hasDifferences(), myDiff.differences.fold(actual, { current, newOne -> current + '\n' + newOne }))
        }

        companion object {
            @Parameters(name = "test {index}: {0} is valid")
            @JvmStatic
            fun data(): Collection<Array<Any>> {
                return listOf(
                        arrayOf("<workout_file><name/></workout_file>",
                                PersistableWorkout()),
                        arrayOf("<workout_file><name/><description>toto</description></workout_file>",
                                PersistableWorkout(description = "toto")),
                        arrayOf("<workout_file><name/><description> </description></workout_file>",
                                PersistableWorkout(description = " ")),
                        arrayOf("<workout_file><name/><sportType>bike</sportType></workout_file>",
                                PersistableWorkout(sportType = SportType.BIKE)),
                        arrayOf("<workout_file><name/><workout/></workout_file>",
                                PersistableWorkout(efforts = Efforts())),
                        arrayOf("<workout_file><name/><workout>" +
                                "<SteadyState Duration='120' Power='0.1'/>" +
                                "</workout></workout_file>",
                                PersistableWorkout(efforts = Efforts(listOf<Effort>(SteadyState(120, 0.1f))))),
                        arrayOf("<workout_file><name/><workout>" +
                                "<Ramp Duration='120' PowerLow='0.1' PowerHigh='0.3'/>" +
                                "<SteadyState Duration='120' Power='0.1'/>" +
                                "</workout></workout_file>",
                                PersistableWorkout(efforts = Efforts(listOf(
                                        Ramp(120, 0.1f, 0.3f),
                                        SteadyState(120, 0.1f))
                                )))
                )
            }
        }
    }

}
