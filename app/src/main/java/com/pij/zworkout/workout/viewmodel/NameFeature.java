package com.pij.zworkout.workout.viewmodel;

import android.support.annotation.NonNull;

import com.pij.horrocks.Interaction;
import com.pij.horrocks.Reducer;
import com.pij.zworkout.workout.Model;

/**
 * <p>Created on 01/03/2018.</p>
 *
 * @author Pierrejean
 */

public class NameFeature implements Interaction<String, Model> {

    @NonNull
    @Override
    public Reducer<Model> process(@NonNull String name) {
        return current -> current.toBuilder()
                .name(name)
                .build();
    }
}
