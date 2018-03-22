package com.pij.zworkout.workout.viewmodel;

import android.support.annotation.NonNull;

import com.annimon.stream.Optional;
import com.pij.horrocks.AsyncInteraction;
import com.pij.horrocks.Configuration;
import com.pij.horrocks.Engine;
import com.pij.horrocks.Interaction;
import com.pij.horrocks.MultipleReducerCreator;
import com.pij.horrocks.ReducerCreator;
import com.pij.horrocks.SingleReducerCreator;
import com.pij.horrocks.Storage;
import com.pij.utils.Logger;
import com.pij.zworkout.service.api.WorkoutFile;
import com.pij.zworkout.uc.Workout;
import com.pij.zworkout.workout.Model;
import com.pij.zworkout.workout.State;
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
    private final ReducerCreator<String, State> loader;
    private final ReducerCreator<Object, State> createWorkout;
    private final ReducerCreator<Object, State> save;
    private final ReducerCreator<String, State> name;

    private HorrocksWorkoutViewModel(Logger logger,
                                     Engine<State, Model> engine,
                                     Storage<State> storage,
                                     ReducerCreator<String, State> nameFeature,
                                     ReducerCreator<String, State> loadingFeature,
                                     ReducerCreator<Object, State> saveFeature,
                                     ReducerCreator<Object, State> createWorkoutFeature
    ) {
        this.logger = logger;

        name = nameFeature;
        loader = loadingFeature;
        save = saveFeature;
        createWorkout = createWorkoutFeature;
        Configuration<State, Model> engineConfiguration = Configuration.<State, Model>builder()
                .store(storage)
                .transientResetter(this::resetTransient)
                .stateToModel(this::convert)
                .creators(asList(name, loader, save, createWorkout))
                .build();
        modelStream = engine.runWith(engineConfiguration).share();
    }

    /**
     * Helper constructor.
     */
    public static HorrocksWorkoutViewModel create(Logger logger,
                                                  Engine<State, Model> engine,
                                                  Storage<State> storage,
                                                  Interaction<String, State> nameFeature,
                                                  AsyncInteraction<String, State> loadingFeature,
                                                  AsyncInteraction<Object, State> saveFeature,
                                                  Interaction<Object, State> createWorkoutFeature
    ) {
        return new HorrocksWorkoutViewModel(logger, engine, storage,
                new SingleReducerCreator<>(nameFeature, logger),
                new MultipleReducerCreator<>(loadingFeature, logger),
                new MultipleReducerCreator<>(saveFeature, logger),
                new SingleReducerCreator<>(createWorkoutFeature, logger)
        );
    }

    public static State initialState() {
        return State.builder()
                .inProgress(false)
                .showError(Optional.empty())
                .showSaved(false)
                .workout(Workout.EMPTY)
                .file(WorkoutFile.UNDEFINED)
                .build();
    }

    @NonNull
    private State resetTransient(@NonNull State input) {
        return input.toBuilder()
                // TODO code that
                .build();
    }

    @NonNull
    private Model convert(@NonNull State state) {
        return Model.builder()
                .inProgress(state.inProgress())
                .showSaved(state.showSaved())
                .showError(state.showError())
                .name(state.workout().name())
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
