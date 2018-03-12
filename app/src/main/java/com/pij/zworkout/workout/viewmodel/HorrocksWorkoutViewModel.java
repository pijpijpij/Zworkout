package com.pij.zworkout.workout.viewmodel;

import android.support.annotation.NonNull;

import com.annimon.stream.Optional;
import com.pij.horrocks.AsyncInteraction;
import com.pij.horrocks.Configuration;
import com.pij.horrocks.Engine;
import com.pij.horrocks.Interaction;
import com.pij.horrocks.Logger;
import com.pij.horrocks.MemoryStorage;
import com.pij.horrocks.MultipleReducerCreator;
import com.pij.horrocks.ReducerCreator;
import com.pij.horrocks.SingleReducerCreator;
import com.pij.zworkout.workout.Model;
import com.pij.zworkout.workout.WorkoutViewModel;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;

import static java.util.Arrays.asList;

/**
 * <p>Created on 01/03/2018.</p>
 *
 * @author Pierrejean
 */

public class HorrocksWorkoutViewModel implements WorkoutViewModel {

    private final Logger logger;
    private final Observable<Model> modelStream;
    private final ReducerCreator<String, Model> loader;
    private final ReducerCreator<Object, Model> createWorkout;
    private final ReducerCreator<Object, Model> save;
    private final ReducerCreator<String, Model> name;

    private HorrocksWorkoutViewModel(Logger logger, Engine<Model, Model> engine,
                                     ReducerCreator<String, Model> nameFeature,
                                     ReducerCreator<String, Model> loadingFeature,
                                     ReducerCreator<Object, Model> saveFeature,
                                     ReducerCreator<Object, Model> createWorkoutFeature) {
        this.logger = logger;

        name = nameFeature;
        loader = loadingFeature;
        save = saveFeature;
        createWorkout = createWorkoutFeature;
        Configuration<Model, Model> engineConfiguration = Configuration.<Model, Model>builder()
                .store(new MemoryStorage<>(initialState()))
                .transientResetter(this::resetTransient)
                .stateToModel(it -> it)
                .creators(asList(name, loader, save, createWorkout))
                .build();
        modelStream = engine.runWith(engineConfiguration).share();
    }

    /**
     * Helper constructor.
     */
    public static HorrocksWorkoutViewModel create(Logger logger,
                                                  Engine<Model, Model> engine,
                                                  Interaction<String, Model> nameFeature,
                                                  AsyncInteraction<String, Model> loadingFeature,
                                                  AsyncInteraction<Object, Model> saveFeature,
                                                  Interaction<Object, Model> createWorkoutFeature
    ) {
        return new HorrocksWorkoutViewModel(logger, engine,
                new SingleReducerCreator<>(nameFeature, logger),
                new MultipleReducerCreator<>(loadingFeature, logger),
                new MultipleReducerCreator<>(saveFeature, logger),
                new SingleReducerCreator<>(createWorkoutFeature, logger)
        );
    }

    @NonNull
    private Model resetTransient(Model input) {
        return input.toBuilder()
                // TODO code that
                .build();
    }

    private Model initialState() {
        return Model.builder()
                .inProgress(false)
                .showError(Optional.empty())
                .name("")
                .build();
    }

    @Override
    public void load(@NonNull String itemId) {
        loader.trigger(itemId);
    }

    @Override
    public void createWorkout() {
        createWorkout.trigger(new Object());
    }

    @NotNull
    @Override
    public Observable<Model> model() {
        return modelStream
                .doOnError(e -> logger.print(getClass(), "Terminal Damage!!!", e))
                .doOnComplete(() -> logger.print(getClass(), "model() completed!!!"))
                ;
    }

    @Override
    public void name(@NotNull String newValue) {
        name.trigger(newValue);
    }

    @Override
    public void save() {
        save.trigger(new Object());
    }
}
