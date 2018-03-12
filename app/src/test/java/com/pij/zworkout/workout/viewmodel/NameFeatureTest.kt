package com.pij.zworkout.workout.viewmodel

import com.annimon.stream.Optional
import com.pij.zworkout.workout.Model
import junit.framework.TestCase.assertEquals
import org.junit.Test


/**
 *
 * Created on 01/03/2018.
 *
 * @author Pierrejean
 */
class NameFeatureTest {

    @Test
    fun `Reducer puts new name in the model`() {
        // given
        val current = Model.create(true, Optional.empty(), "the original")
        val sut = NameFeature()

        // when
        val next = sut.process("the new one").reduce(current)

        // then
        assertEquals(next, Model.create(true, Optional.empty(), "the new one"))

    }

}