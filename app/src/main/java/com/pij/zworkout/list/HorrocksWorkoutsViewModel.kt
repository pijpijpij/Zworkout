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
                .transientResetter { this.resetTransient(it) }
                .stateToModel { it -> it }
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
