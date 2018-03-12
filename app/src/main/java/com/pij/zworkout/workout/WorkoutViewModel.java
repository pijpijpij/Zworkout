package com.pij.zworkout.workout;

import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;

/**
 * <p>Created on 01/03/2018.</p>
 *
 * @author Pierrejean
 */

public interface WorkoutViewModel {

    @NotNull
    Observable<Model> model();

    void load(@NonNull String itemId);

    void createWorkout();

    void name(@NotNull String newValue);

    void save();
}
