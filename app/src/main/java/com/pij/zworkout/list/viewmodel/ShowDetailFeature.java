package com.pij.zworkout.list.viewmodel;

import com.annimon.stream.Optional;
import com.pij.horrocks.Result;
import com.pij.zworkout.list.Model;
import com.pij.zworkout.list.WorkoutInfo;

import io.reactivex.functions.Function;

/**
 * <p>Created on 01/03/2018.</p>
 *
 * @author Pierrejean
 */

public class ShowDetailFeature implements Function<WorkoutInfo, Result<Model>> {
    @Override
    public Result<Model> apply(WorkoutInfo workoutInfo) {
        return current -> current.toBuilder()
                .showWorkout(Optional.of(workoutInfo))
                .build();
    }
}
