package com.pij.zworkout.workout.viewmodel;

import android.support.annotation.NonNull;

import com.pij.horrocks.Interaction;
import com.pij.horrocks.Reducer;
import com.pij.zworkout.workout.State;

import javax.inject.Provider;

/**
 * <p>Created on 01/03/2018.</p>
 *
 * @author Pierrejean
 */

public class CreateWorkoutFeature implements Interaction<Object, State> {
    private final Provider<String> name;

    public CreateWorkoutFeature(Provider<String> name) {
        this.name = name;
    }

    @NonNull
    @Override
    public Reducer<State> process(@NonNull Object event) {
        return current -> current.toBuilder()
                .workout(current.workout().name(name.get()))
                .file(current.file().toBuilder().name(name.get()).build())
                .build();
    }

}
