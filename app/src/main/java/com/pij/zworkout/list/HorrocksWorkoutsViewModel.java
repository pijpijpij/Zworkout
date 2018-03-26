package com.pij.zworkout.list;

import android.support.annotation.NonNull;

import com.annimon.stream.Optional;
import com.pij.horrocks.AsyncInteraction;
import com.pij.horrocks.Configuration;
import com.pij.horrocks.Engine;
import com.pij.horrocks.Interaction;
import com.pij.horrocks.MemoryStorage;
import com.pij.horrocks.MultipleReducerCreator;
import com.pij.horrocks.ReducerCreator;
import com.pij.horrocks.SingleReducerCreator;
import com.pij.utils.Logger;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

/**
 * <p>Created on 01/03/2018.</p>
 *
 * @author Pierrejean
 */

class HorrocksWorkoutsViewModel implements WorkoutsViewModel {

    private final Logger logger;
    private final Observable<Model> modelStream;
    private final ReducerCreator<Object, Model> loader;
    private final ReducerCreator<WorkoutInfo, Model> showDetail;
    private final ReducerCreator<Object, Model> createWorkout;

    private HorrocksWorkoutsViewModel(Logger logger, Engine<Model, Model> engine,
                                      ReducerCreator<Object, Model> loadingFeature,
                                      ReducerCreator<WorkoutInfo, Model> showDetailFeature,
                                      ReducerCreator<Object, Model> createWorkoutFeature) {
        this.logger = logger;

        loader = loadingFeature;
        showDetail = showDetailFeature;
        createWorkout = createWorkoutFeature;
        Configuration<Model, Model> engineConfiguration = Configuration.<Model, Model>builder()
                .store(new MemoryStorage<>(initialState()))
                .transientResetter(this::resetTransient)
                .stateToModel(it -> it)
                .creators(asList(loader, showDetail, createWorkout))
                .build();
        modelStream = engine.runWith(engineConfiguration).share();
    }

    /**
     * Helper constructor.
     */
    public static HorrocksWorkoutsViewModel create(Logger logger,
                                                   Engine<Model, Model> engine,
                                                   AsyncInteraction<Object, Model> loadingFeature,
                                                   Interaction<WorkoutInfo, Model> showDetailFeature,
                                                   Interaction<Object, Model> createWorkoutFeature
    ) {
        return new HorrocksWorkoutsViewModel(logger, engine,
                new MultipleReducerCreator<>(loadingFeature, logger),
                new SingleReducerCreator<>(showDetailFeature, logger),
                new SingleReducerCreator<>(createWorkoutFeature, logger)
        );
    }

    @NonNull
    private Model resetTransient(Model input) {
        return input.toBuilder()
                .showWorkout(Optional.empty())
                .showError(Optional.empty())
                .createWorkout(false)
                .build();
    }

    @NonNull
    private Model initialState() {
        return Model.create(false, Optional.empty(), Optional.empty(), false, emptyList());
    }

    @Override
    public void load() {
        loader.trigger(new Object());
    }

    @Override
    public void select(@NonNull WorkoutInfo workout) {
        showDetail.trigger(workout);
    }

    @Override
    public void createWorkout() {
        createWorkout.trigger(new Object());
    }

    @NotNull
    @Override
    public Observable<Model> model() {
        return modelStream
                .doOnError(e -> logger.print(getClass(), e, "Terminal Damage!!!"))
                .doOnComplete(() -> logger.print(getClass(), "model() completed!!!"))
                ;
    }
}
