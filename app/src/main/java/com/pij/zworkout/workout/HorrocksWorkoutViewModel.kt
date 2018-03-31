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

package com.pij.zworkout.workout

import com.pij.horrocks.*
import com.pij.utils.Logger
import com.pij.zworkout.R.id.duration
import com.pij.zworkout.uc.Workout
import io.reactivex.Observable

/**
 *
 * Created on 01/03/2018.
 *
 * @author Pierrejean
 */

internal class HorrocksWorkoutViewModel private constructor(private val logger: Logger,
                                                            engine: Engine<State, Model>,
                                                            storage: Storage<State>,
                                                            private val name: ReducerCreator<String, State>,
                                                            private val description: ReducerCreator<String, State>,
                                                            private val loader: ReducerCreator<String, State>,
                                                            private val save: ReducerCreator<Any, State>,
                                                            private val createWorkout: ReducerCreator<Any, State>
) : WorkoutViewModel {
    private val models: Observable<Model>

    init {
        val engineConfiguration = Configuration.builder<State, Model>()
                .store(storage)
                .transientResetter { this.resetTransient(it) }
                .stateToModel { this.convert(it) }
                .creators(listOf(name, description, loader, save, createWorkout))
                .build()
        models = engine.runWith(engineConfiguration).share()
    }

    private fun resetTransient(input: State): State {
        return input.copy(
                showError = null,
                showSaved = false
                // TODO code that
        )
    }

    private fun convert(state: State): Model {
        return with(state) {
            Model(
                    inProgress = inProgress,
                    showSaved = showSaved,
                    showError = showError,
                    name = workout.name,
                    nameIsReadOnly = nameIsReadOnly,
                    description = workout.description,
                    efforts = workout.efforts.map { convert(it) }
            )
        }
    }

    private fun convert(state: com.pij.zworkout.uc.Effort): Effort {
        return with(state) {
            when (state) {
                is com.pij.zworkout.uc.SteadyState -> SteadyState(duration = duration, power = PowerRange.Z1)
                else -> TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
    }

    override fun load(itemId: String) {
        loader.trigger(itemId)
    }

    override fun createWorkout() {
        createWorkout.trigger(Any())
    }

    override fun model(): Observable<Model> {
        return models
                .doOnError { e -> logger.print(javaClass, e, "Terminal Damage!!!") }
                .doOnComplete { logger.print(javaClass, "model() completed!!!") }
    }

    override fun name(newValue: String) {
        name.trigger(newValue)
    }

    override fun description(newValue: String) {
        description.trigger(newValue)
    }

    override fun save() {
        save.trigger(Any())
    }

    companion object {

        /**
         * Helper constructor.
         */
        fun create(logger: Logger,
                   engine: Engine<State, Model>,
                   storage: Storage<State>,
                   nameFeature: Interaction<String, State>,
                   descriptionFeature: Interaction<String, State>,
                   loadingFeature: AsyncInteraction<String, State>,
                   saveFeature: AsyncInteraction<Any, State>,
                   createWorkoutFeature: Interaction<Any, State>
        ): HorrocksWorkoutViewModel {
            return HorrocksWorkoutViewModel(logger, engine, storage,
                    SingleReducerCreator(nameFeature, logger),
                    SingleReducerCreator(descriptionFeature, logger),
                    MultipleReducerCreator(loadingFeature, logger),
                    MultipleReducerCreator(saveFeature, logger),
                    SingleReducerCreator(createWorkoutFeature, logger)
            )
        }

        fun initialState(): State {
            return State(inProgress = false,
                    showError = null,
                    showSaved = false,
                    workout = Workout(),
                    file = null,
                    nameIsReadOnly = false)
        }
    }
}
