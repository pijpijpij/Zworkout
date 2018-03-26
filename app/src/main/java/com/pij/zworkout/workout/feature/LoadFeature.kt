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

package com.pij.zworkout.workout.feature

import com.annimon.stream.Optional
import com.annimon.stream.Optional.of
import com.pij.horrocks.AsyncInteraction
import com.pij.horrocks.Reducer
import com.pij.utils.Logger
import com.pij.zworkout.uc.Workout
import com.pij.zworkout.uc.WorkoutPersistenceUC
import com.pij.zworkout.workout.State
import io.reactivex.Observable
import java.io.File
import java.net.URI

/**
 *
 * Created on 01/01/2018.
 *
 * @author PierreJean
 */
class LoadFeature(
        private val logger: Logger,
        private val storage: WorkoutPersistenceUC,
        private val defaultErrorMessage: String
) : AsyncInteraction<String, State> {

    private fun updateSuccessState(current: State, workout: Workout, file: File): State {
        return current.toBuilder()
                .workout(workout)
                .file(Optional.of(file))
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
        return Observable.just(workoutId)
                .map { it -> URI.create(it) }
                .map { File(it) }
                .flatMapSingle { file ->
                    storage.load(file)
                            .doOnError { logger.print(this.javaClass, it, "Could not load data") }
                            .map { workout -> Reducer<State> { current -> updateSuccessState(current, workout, file) } }
                }
                .onErrorReturn { e -> Reducer { updateFailureState(it, e) } }
                .startWith(Reducer { updateStartState(it) })
    }
}
