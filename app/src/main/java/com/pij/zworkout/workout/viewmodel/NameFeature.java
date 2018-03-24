package com.pij.zworkout.workout.viewmodel;

import android.support.annotation.NonNull;

import com.pij.horrocks.Interaction;
import com.pij.horrocks.Reducer;
import com.pij.zworkout.workout.State;

/**
 * <p>Created on 01/03/2018.</p>
 *
 * @author Pierrejean
 */

public class NameFeature implements Interaction<String, State> {

    @NonNull
    @Override
    public Reducer<State> process(@NonNull String name) {
        // TODO change the name of the file if it does not exist
        return current -> current.toBuilder()
                .workout(current.workout().toBuilder().name(name).build())
                .build();
    }
}
