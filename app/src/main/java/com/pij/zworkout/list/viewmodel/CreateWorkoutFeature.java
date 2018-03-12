package com.pij.zworkout.list.viewmodel;

import android.support.annotation.NonNull;

import com.pij.horrocks.Interaction;
import com.pij.horrocks.Reducer;
import com.pij.zworkout.list.Model;

/**
 * <p>Created on 01/03/2018.</p>
 *
 * @author Pierrejean
 */

public class CreateWorkoutFeature implements Interaction<Object, Model> {
    @NonNull
    @Override
    public Reducer<Model> process(@NonNull Object event) {
        return current -> current.toBuilder()
                .createWorkout(true)
                .build();
    }
}
