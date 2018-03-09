package com.pij.zworkout.workout.viewmodel;

import com.pij.horrocks.Result;
import com.pij.zworkout.workout.Model;

import io.reactivex.functions.Function;

/**
 * <p>Created on 01/03/2018.</p>
 *
 * @author Pierrejean
 */

public class NameFeature implements Function<String, Result<Model>> {

    @Override
    public Result<Model> apply(String name) {
        return current -> current.toBuilder()
                .name(name)
                .build();
    }
}
