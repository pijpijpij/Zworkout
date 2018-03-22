package com.pij.zworkout.persistence.api

import org.simpleframework.xml.core.Persister
import org.simpleframework.xml.core.ValueRequiredException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull


/**
 *
 * Created on 13/03/2018.
 *
 * @author Pierrejean
 */
class PersistableWorkoutTest {

    private lateinit var serializer: Persister

    @BeforeTest
    fun setUp() {
        serializer = Persister()

    }

    @Test(expected = ValueRequiredException::class)
    fun `Deserialization of empty workout fails`() {
        // given
        val xml = "<workout_file/>"

        // when
        serializer.read(PersistableWorkout::class.java, xml)

        // then
    }

    @Test(expected = ValueRequiredException::class)
    fun `Deserialization of workout with an empty name works`() {
        // given
        val xml = "<workout_file><name/></workout_file>"

        // when
        val workout = serializer.read(PersistableWorkout::class.java, xml)

        // then
        assertNotNull(workout)
    }
}