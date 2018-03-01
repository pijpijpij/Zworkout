package com.pij.zworkout.list;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;

import io.reactivex.Observable;

/**
 * <p>Created on 01/03/2018.</p>
 *
 * @author Pierrejean
 */

class DefaultViewModel implements ViewModel {
    @Override
    public void load() {

    }

    @NotNull
    @Override
    public Observable<Model> model() {
        return Observable.just(Model.builder()
                .inProgress(false)
                .workouts(Collections.emptyList())
                .build());
    }
}
