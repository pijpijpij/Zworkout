package com.pij.zworkout.workout;


import android.content.res.Resources;

import com.pij.horrocks.DefaultEngine;
import com.pij.horrocks.MemoryStorage;
import com.pij.horrocks.Storage;
import com.pij.utils.Logger;
import com.pij.zworkout.FragmentScoped;
import com.pij.zworkout.R;
import com.pij.zworkout.uc.WorkoutPersistenceUC;
import com.pij.zworkout.workout.viewmodel.CreateWorkoutFeature;
import com.pij.zworkout.workout.viewmodel.DescriptionFeature;
import com.pij.zworkout.workout.viewmodel.HorrocksWorkoutViewModel;
import com.pij.zworkout.workout.viewmodel.LoadFeature;
import com.pij.zworkout.workout.viewmodel.NameFeature;
import com.pij.zworkout.workout.viewmodel.SaveFeature;

import dagger.Module;
import dagger.Provides;

/**
 * This is a Dagger module. We use this to pass in the View dependency to the {@link WorkoutViewModel}.
 */
@Module
public class WorkoutDetailModule {

    @FragmentScoped
    @Provides
    WorkoutViewModel provideHorrocksViewModel(Logger logger,
                                              Storage<State> storage,
                                              LoadFeature loadingFeature,
                                              SaveFeature saveFeature,
                                              CreateWorkoutFeature createWorkoutFeature) {
        return HorrocksWorkoutViewModel.create(logger,
                new DefaultEngine<>(logger),
                storage,
                new NameFeature(),
                new DescriptionFeature(),
                loadingFeature,
                saveFeature,
                createWorkoutFeature
        );
    }


    @FragmentScoped
    @Provides
    Storage<State> provideMemoryStorage() {
        return new MemoryStorage<>(HorrocksWorkoutViewModel.initialState());
    }

    @Provides
    CreateWorkoutFeature provideCreateWorkoutFeature() {
        // TODO use a real name calculator
        return new CreateWorkoutFeature(() -> "Unnamed workout");
    }

    @Provides
    LoadFeature provideStorageLoadingFeature(Logger logger, WorkoutPersistenceUC storage, Resources resources) {
        String defaultErrorMessage = resources.getString(R.string.list_loading_error_message);
        return new LoadFeature(logger, storage, defaultErrorMessage);
    }

    @Provides
    SaveFeature provideSaveFeature(Logger logger, WorkoutPersistenceUC storage, Storage<State> stateProvider, Resources resources) {
        String defaultErrorMessage = resources.getString(R.string.workout_save_error_message);
        return new SaveFeature(logger, storage, stateProvider::load, defaultErrorMessage);
    }

}