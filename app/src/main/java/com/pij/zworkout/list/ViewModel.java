package com.pij.zworkout.list;

import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;

/**
 * <p>Created on 01/03/2018.</p>
 *
 * @author Pierrejean
 */

public interface ViewModel {

    void load();

    void select(@NonNull WorkoutInfo workout);

    void createWorkout();

    @NotNull
    Observable<Model> model();
}
