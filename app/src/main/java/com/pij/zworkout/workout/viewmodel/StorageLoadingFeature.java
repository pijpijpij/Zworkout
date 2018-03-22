/*
 * Copyright 2018, Chiswick Forest
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.pij.zworkout.workout.viewmodel;

import android.support.annotation.NonNull;

import com.pij.horrocks.AsyncInteraction;
import com.pij.horrocks.Logger;
import com.pij.horrocks.Reducer;
import com.pij.zworkout.service.api.StorageService;
import com.pij.zworkout.workout.State;

import io.reactivex.Observable;

import static com.annimon.stream.Optional.of;

/**
 * <p>Created on 01/01/2018.</p>
 *
 * @author PierreJean
 */
public class StorageLoadingFeature implements AsyncInteraction<String, State> {

    private final String defaultErrorMessage;
    private final Logger logger;
    private final StorageService storage;

    public StorageLoadingFeature(Logger logger, StorageService storage, String defaultErrorMessage) {
        this.logger = logger;
        this.storage = storage;
        this.defaultErrorMessage = defaultErrorMessage;
    }

    @NonNull
    private static State updateSuccessState(State current/*, List<WorkoutDescriptor> list*/) {
        return current.toBuilder()
                // TODO write this
                .inProgress(false)
                .build();
    }

    @NonNull
    private State updateFailureState(State current, Throwable error) {
        String errorMessage = error.getMessage();
        String actualMessage = errorMessage == null ? defaultErrorMessage : errorMessage;
        return current.toBuilder()
                .showError(of(actualMessage))
                .inProgress(false)
                .build();
    }

    @NonNull
    private State updateStartState(State current) {
        return current.toBuilder()
                .inProgress(true)
                .build();
    }

    @NonNull
    @Override
    public Observable<Reducer<State>> process(@NonNull String workoutId) {
        return
                Observable.just(current -> updateFailureState(current,
                        new UnsupportedOperationException("apply([workoutId]) not implemented.")));
//        return storage.workout(workoutId)
//                .doOnError(e -> logger.print(getClass(), "Could not load data", e))
//                .flatMapSingle(files -> Observable.fromIterable(files).map(this::convert).toList())
//                .map(descriptions -> (Reducer<Model>) current -> updateSuccessState(current, descriptions))
//                .onErrorReturn(e -> current -> updateFailureState(current, e))
//                .startWith(this::updateStartState);
    }

}
