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
import com.pij.zworkout.uc.Workout
import com.pij.zworkout.workout.feature.InsertEffortFeature
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
                                                            private val converter: StateConverter<State, Model>,
                                                            private val name: ReducerCreator<String, State>,
                                                            private val description: ReducerCreator<String, State>,
                                                            private val loader: ReducerCreator<String, State>,
                                                            private val save: ReducerCreator<Any, State>,
                                                            private val insertEffort: ReducerCreator<Pair<ModelEffort, Int>, State>,
                                                            private val setEffort: ReducerCreator<Pair<ModelEffort, Int>, State>,
                                                            private val editEffortProperty: ReducerCreator<EffortPropertyEvent, State>,
                                                            private val createWorkout: ReducerCreator<Any, State>
) : WorkoutViewModel {
    private val models: Observable<Model>

    init {
        val engineConfiguration = Configuration.builder<State, Model>()
                .store(storage)
                .transientResetter { resetTransient(it) }
                .stateToModel { converter.convert(it) }
                .creators(listOf(name, description, loader, save, insertEffort, setEffort, editEffortProperty, createWorkout))
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

    override fun addEffort() {
        insertEffort.trigger(Pair(ModelSteadyState(120, "Z1"), InsertEffortFeature.END_OF_LIST))
    }

    override fun setEffort(effort: ModelEffort, position: Int) {
        setEffort.trigger(Pair(effort, position))
    }

    override fun editEffortProperty(description: EffortPropertyEvent) {
        editEffortProperty.trigger(description)
    }

    override fun changeEffortProperty(description: EffortPropertyEvent) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {

        /**
         * Helper constructor.
         */
        fun create(logger: Logger,
                   engine: Engine<State, Model>,
                   storage: Storage<State>,
                   converter: StateConverter<State, Model>,
                   nameFeature: Interaction<String, State>,
                   descriptionFeature: Interaction<String, State>,
                   loadingFeature: AsyncInteraction<String, State>,
                   saveFeature: AsyncInteraction<Any, State>,
                   insertEffortFeature: Interaction<Pair<ModelEffort, Int>, State>,
                   setEffortFeature: Interaction<Pair<ModelEffort, Int>, State>,
                   editEffortPropertyFeature: Interaction<EffortPropertyEvent, State>,
                   createWorkoutFeature: Interaction<Any, State>
        ): HorrocksWorkoutViewModel {
            return HorrocksWorkoutViewModel(logger, engine, storage,
                    converter,
                    SingleReducerCreator(nameFeature, logger),
                    SingleReducerCreator(descriptionFeature, logger),
                    MultipleReducerCreator(loadingFeature, logger),
                    MultipleReducerCreator(saveFeature, logger),
                    SingleReducerCreator(insertEffortFeature, logger),
                    SingleReducerCreator(setEffortFeature, logger),
                    SingleReducerCreator(editEffortPropertyFeature, logger),
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
