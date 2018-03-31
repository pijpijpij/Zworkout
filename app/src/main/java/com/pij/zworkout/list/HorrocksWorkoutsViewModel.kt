/*
 * Copyright (c) 2018, Chiswick Forest
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

package com.pij.zworkout.list

import com.pij.horrocks.*
import com.pij.utils.Logger
import io.reactivex.Observable

/**
 *
 * Created on 01/03/2018.
 *
 * @author Pierrejean
 */

internal class HorrocksWorkoutsViewModel private constructor(private val logger: Logger, engine: Engine<Model, Model>,
                                                             private val loader: ReducerCreator<Any, Model>,
                                                             private val showDetail: ReducerCreator<WorkoutInfo, Model>,
                                                             private val createWorkout: ReducerCreator<Any, Model>) : WorkoutsViewModel {
    private val modelStream: Observable<Model>

    init {
        val engineConfiguration = Configuration.builder<Model, Model>()
                .store(MemoryStorage(initialState()))
                .transientResetter { resetTransient(it) }
                .stateToModel { it }
                .creators(listOf(loader, showDetail, createWorkout))
                .build()
        modelStream = engine.runWith(engineConfiguration).share()
    }

    private fun resetTransient(input: Model): Model {
        return input.copy(
                showWorkout = null,
                showError = null,
                createWorkout = false)
    }

    private fun initialState(): Model {
        return Model(false, null, null, false, emptyList())
    }

    override fun load() {
        loader.trigger(Any())
    }

    override fun select(workout: WorkoutInfo) {
        showDetail.trigger(workout)
    }

    override fun createWorkout() {
        createWorkout.trigger(Any())
    }

    override fun model(): Observable<Model> {
        return modelStream
                .doOnError { e -> logger.print(javaClass, e, "Terminal Damage!!!") }
                .doOnComplete { logger.print(javaClass, "model() completed!!!") }
    }

    companion object {

        /**
         * Helper constructor.
         */
        fun create(logger: Logger,
                   engine: Engine<Model, Model>,
                   loadingFeature: AsyncInteraction<Any, Model>,
                   showDetailFeature: Interaction<WorkoutInfo, Model>,
                   createWorkoutFeature: Interaction<Any, Model>
        ): HorrocksWorkoutsViewModel {
            return HorrocksWorkoutsViewModel(logger, engine,
                    MultipleReducerCreator(loadingFeature, logger),
                    SingleReducerCreator(showDetailFeature, logger),
                    SingleReducerCreator(createWorkoutFeature, logger)
            )
        }
    }
}
