package com.pij.zworkout.uc;

import com.pij.zworkout.persistence.api.PersistableWorkout;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created on 16/03/2018.
 *
 * @author Pierrejean
 */
public class PersistableWorkoutConverterTest {

    private PersistableWorkoutConverter sut;

    @Before
    public void setUp() {
        sut = new PersistableWorkoutConverter();
    }

    @Test
    public void copiesName() {
        // given
        Workout in = Workout.builder().name("hello").build();

        // when
        PersistableWorkout result = sut.convert(in);

        // then
        assertEquals(result.getName(), "hello");

    }
}