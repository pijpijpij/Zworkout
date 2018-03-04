package com.pij.zworkout.list.viewmodel;

import com.pij.horrocks.Result;
import com.pij.zworkout.list.Model;

import io.reactivex.functions.Function;

/**
 * <p>Created on 01/03/2018.</p>
 *
 * @author Pierrejean
 */

public class CreateWorkoutFeature implements Function<Object, Result<Model>> {
    @Override
    public Result<Model> apply(Object event) {
        return current -> current.toBuilder()
                .createWorkout(true)
                .build();
    }
}
