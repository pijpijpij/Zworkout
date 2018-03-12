package com.pij.zworkout.list.viewmodel;

import android.support.annotation.NonNull;

import com.annimon.stream.Optional;
import com.pij.horrocks.Interaction;
import com.pij.horrocks.Reducer;
import com.pij.zworkout.list.Model;
import com.pij.zworkout.list.WorkoutInfo;

/**
 * <p>Created on 01/03/2018.</p>
 *
 * @author Pierrejean
 */

public class ShowDetailFeature implements Interaction<WorkoutInfo, Model> {
    @NonNull
    @Override
    public Reducer<Model> process(@NonNull WorkoutInfo workoutInfo) {
        return current -> current.toBuilder()
                .showWorkout(Optional.of(workoutInfo))
                .build();
    }
}
