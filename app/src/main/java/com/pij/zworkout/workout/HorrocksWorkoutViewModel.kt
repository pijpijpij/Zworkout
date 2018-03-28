package com.pij.zworkout.workout

import com.pij.horrocks.*
import com.pij.utils.Logger
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
        return Model(
                inProgress = state.inProgress,
                showSaved = state.showSaved,
                showError = state.showError,
                name = state.workout.name,
                nameIsReadOnly = state.nameIsReadOnly,
                description = state.workout.description)
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
