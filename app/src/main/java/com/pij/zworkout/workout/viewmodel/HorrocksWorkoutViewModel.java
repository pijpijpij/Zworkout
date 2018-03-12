package com.pij.zworkout.workout.viewmodel;

import android.support.annotation.NonNull;

import com.annimon.stream.Optional;
import com.pij.horrocks.Configuration;
import com.pij.horrocks.Engine;
import com.pij.horrocks.Feature;
import com.pij.horrocks.Logger;
import com.pij.horrocks.MemoryStore;
import com.pij.horrocks.MultipleResultFeature;
import com.pij.horrocks.Result;
import com.pij.horrocks.SingleResultFeature;
import com.pij.zworkout.workout.Model;
import com.pij.zworkout.workout.WorkoutViewModel;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

import static java.util.Arrays.asList;

/**
 * <p>Created on 01/03/2018.</p>
 *
 * @author Pierrejean
 */

public class HorrocksWorkoutViewModel implements WorkoutViewModel {

    private final Logger logger;
    private final Observable<Model> modelStream;
    private final Feature<String, Model> loader;
    private final Feature<Object, Model> createWorkout;
    private final Feature<Object, Model> save;
    private final Feature<String, Model> name;

    private HorrocksWorkoutViewModel(Logger logger, Engine<Model, Model> engine,
                                     Feature<String, Model> nameFeature,
                                     Feature<String, Model> loadingFeature,
                                     Feature<Object, Model> saveFeature,
                                     Feature<Object, Model> createWorkoutFeature) {
        this.logger = logger;

        name = nameFeature;
        loader = loadingFeature;
        save = saveFeature;
        createWorkout = createWorkoutFeature;
        Configuration<Model, Model> engineConfiguration = Configuration.<Model, Model>builder()
                .store(new MemoryStore<>(initialState()))
                .transientResetter(this::resetTransient)
                .stateToModel(it -> it)
                .features(asList(name, loader, save, createWorkout))
                .build();
        modelStream = engine.runWith(engineConfiguration).share();
    }

    /**
     * Helper constructor.
     */
    public static HorrocksWorkoutViewModel create(Logger logger,
                                                  Engine<Model, Model> engine,
                                                  Function<String, Result<Model>> nameFeature,
                                                  Function<String, Observable<Result<Model>>> loadingFeature,
                                                  Function<Object, Observable<Result<Model>>> saveFeature,
                                                  Function<Object, Result<Model>> createWorkoutFeature
    ) {
        return new HorrocksWorkoutViewModel(logger, engine,
                new SingleResultFeature<>(nameFeature, logger),
                new MultipleResultFeature<>(loadingFeature, logger),
                new MultipleResultFeature<>(saveFeature, logger),
                new SingleResultFeature<>(createWorkoutFeature, logger)
        );
    }

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
