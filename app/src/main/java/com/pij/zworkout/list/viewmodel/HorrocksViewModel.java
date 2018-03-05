package com.pij.zworkout.list.viewmodel;

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
import com.pij.zworkout.list.Model;
import com.pij.zworkout.list.ViewModel;
import com.pij.zworkout.list.WorkoutInfo;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

/**
 * <p>Created on 01/03/2018.</p>
 *
 * @author Pierrejean
 */

public class HorrocksViewModel implements ViewModel {

    private final Logger logger;
    private final Observable<Model> modelStream;
    private final Feature<Object, Model> loader;
    private final Feature<WorkoutInfo, Model> showDetail;
    private final Feature<Object, Model> createWorkout;

    private HorrocksViewModel(Logger logger, Engine<Model, Model> engine,
                              Feature<Object, Model> loadingFeature,
                              Feature<WorkoutInfo, Model> showDetailFeature,
                              Feature<Object, Model> createWorkoutFeature) {
        this.logger = logger;

        loader = loadingFeature;
        showDetail = showDetailFeature;
        createWorkout = createWorkoutFeature;
        Configuration<Model, Model> engineConfiguration = Configuration.<Model, Model>builder()
                .store(new MemoryStore<>(initialState()))
                .transientResetter(this::resetTransient)
                .stateToModel(it -> it)
                .features(asList(loader, showDetail, createWorkout))
                .build();
        modelStream = engine.runWith(engineConfiguration).share();
    }

    /**
     * Helper constructor.
     */
    public static HorrocksViewModel create(Logger logger,
                                           Engine<Model, Model> engine,
                                           Function<Object, Observable<Result<Model>>> loadingFeature,
                                           Function<WorkoutInfo, Result<Model>> showDetailFeature,
                                           Function<Object, Result<Model>> createWorkoutFeature
    ) {
        return new HorrocksViewModel(logger, engine,
                new MultipleResultFeature<>(loadingFeature, logger),
                new SingleResultFeature<>(showDetailFeature, logger),
                new SingleResultFeature<>(createWorkoutFeature, logger)
        );
    }

    private Model resetTransient(Model input) {
        return input.toBuilder()
                .showWorkout(Optional.empty())
                .showError(Optional.empty())
                .createWorkout(false)
                .build();
    }

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
                .doOnError(e -> logger.print(getClass(), "Terminal Damage!!!", e))
                .doOnComplete(() -> logger.print(getClass(), "model() completed!!!"))
                ;
    }
}
