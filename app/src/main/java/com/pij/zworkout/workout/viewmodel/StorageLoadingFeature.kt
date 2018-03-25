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

package com.pij.zworkout.workout.viewmodel

import com.annimon.stream.Optional.of
import com.pij.horrocks.AsyncInteraction
import com.pij.horrocks.Reducer
import com.pij.utils.Logger
import com.pij.zworkout.uc.WorkoutPersistenceUC
import com.pij.zworkout.workout.State
import io.reactivex.Observable

/**
 *
 * Created on 01/01/2018.
 *
 * @author PierreJean
 */
class StorageLoadingFeature(
        private val logger: Logger,
        private val storage: WorkoutPersistenceUC,
        private val defaultErrorMessage: String
) : AsyncInteraction<String, State> {

    private fun updateSuccessState(current: State/*, List<WorkoutDescriptor> list*/): State {
        return current.toBuilder()
                // TODO write this
                .nameIsReadOnly(false)
                .inProgress(false)
                .build()
    }

    private fun updateFailureState(current: State, error: Throwable): State {
        val errorMessage = error.message
        val actualMessage = errorMessage ?: defaultErrorMessage
        return current.toBuilder()
                .showError(of(actualMessage))
                .inProgress(false)
                .build()
    }

    private fun updateStartState(current: State): State {
        return current.toBuilder()
                .inProgress(true)
                .build()
    }

    override fun process(workoutId: String): Observable<Reducer<State>> {
        return Observable.just(Reducer { current ->
            updateFailureState(current, UnsupportedOperationException("apply([workoutId]) not implemented."))
        })
//                return storage.workout(workoutId)
//                        .doOnError(e -> logger.print(getClass(), e, "Could not load data"))
//                        .flatMapSingle(files -> Observable.fromIterable(files).map(this::convert).toList())
//                        .map(descriptions -> (Reducer<Model>) current -> updateSuccessState(current, descriptions))
//                        .onErrorReturn(e -> current -> updateFailureState(current, e))
//                        .startWith(this::updateStartState);
    }

}
