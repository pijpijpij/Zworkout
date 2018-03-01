package com.pij.zworkout.list

import junit.framework.TestCase.assertFalse
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasSize
import org.junit.Before
import org.junit.Test

/**
 *
 * Created on 01/03/2018.
 *
 * @author Pierrejean
 */
class DefaultViewModelTest {

    private lateinit var sut: DefaultViewModel

    @Before
    fun setUp() {
        sut = DefaultViewModel()
    }

    @Test
    fun `initial model subscription does not fail`() {
        // given

        // when
        val observer = sut.model().test()

        // then
        observer.assertNoErrors()
    }

    @Test
    fun `initial model subscription receives 1 model`() {
        // given

        // when
        val observer = sut.model().test()

        // then
        observer.assertValueCount(1)
    }

    @Test
    fun `initial model subscription receives empty model`() {
        // given

        // when
        val observer = sut.model().test()

        // then
        val result = observer.values()[0]
        assertFalse(result.inProgress())
        assertThat(result.workouts(), hasSize(0))
    }
}