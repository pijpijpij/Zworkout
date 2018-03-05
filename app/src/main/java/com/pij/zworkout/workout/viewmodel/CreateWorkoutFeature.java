package com.pij.zworkout.workout.viewmodel;

import com.pij.horrocks.Result;
import com.pij.zworkout.workout.Model;

import javax.inject.Provider;

import io.reactivex.functions.Function;

/**
 * <p>Created on 01/03/2018.</p>
 *
 * @author Pierrejean
 */

public class CreateWorkoutFeature implements Function<Object, Result<Model>> {
    private final Provider<String> name;

    public CreateWorkoutFeature(Provider<String> name) {
        this.name = name;
    }

    @Override
    public Result<Model> apply(Object event) {
        return current -> current.toBuilder()
                .name(name.get())
                // TODO write this
                .build();
    }
}
