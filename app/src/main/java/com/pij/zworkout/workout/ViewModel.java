package com.pij.zworkout.workout;

import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;

/**
 * <p>Created on 01/03/2018.</p>
 *
 * @author Pierrejean
 */

public interface ViewModel {

    void load(@NonNull String itemId);

    void createWorkout();

    @NotNull
    Observable<Model> model();
}
