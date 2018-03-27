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

package com.pij.zworkout.list.feature

import com.pij.horrocks.AsyncInteraction
import com.pij.horrocks.Reducer
import com.pij.utils.Logger
import com.pij.zworkout.list.Model
import com.pij.zworkout.list.WorkoutInfo
import com.pij.zworkout.service.api.StorageService
import com.pij.zworkout.service.api.WorkoutFile
import io.reactivex.Observable
import io.reactivex.rxkotlin.toObservable

/**
 *
 * Created on 01/01/2018.
 *
 * @author PierreJean
 */
class StorageLoadingFeature(private val logger: Logger, private val storage: StorageService, private val defaultErrorMessage: String) : AsyncInteraction<Any, Model> {

    private fun updateSuccessState(current: Model, list: List<WorkoutInfo>): Model {
        return current.copy(
                workouts = list,
                inProgress = false)
    }

    private fun updateFailureState(current: Model, error: Throwable): Model {
        val actualMessage = error.message ?: defaultErrorMessage
        return current.copy(
                showError = actualMessage,
                inProgress = false)
    }

    private fun updateStartState(current: Model): Model {
        return current.copy(
                inProgress = true)
    }

    override fun process(trigger: Any): Observable<Reducer<Model>> {
        return storage.workouts()
                .doOnError { logger.print(javaClass, "Could not load data", it) }
                .flatMapSingle { files -> files.toObservable().map { this.convert(it) }.toList() }
                .map { descriptions -> Reducer<Model> { current -> updateSuccessState(current, descriptions) } }
                .onErrorReturn { Reducer { current -> updateFailureState(current, it) } }
                .startWith(Reducer { updateStartState(it) })
    }

    private fun convert(file: WorkoutFile): WorkoutInfo {
        return WorkoutInfo(
                id = file.uri.toString(),
                name = file.name,
                // TODO implement details
                details = null)
    }

}
