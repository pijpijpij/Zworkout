package com.pij.zworkout.workout;


import android.content.res.Resources;

import com.pij.horrocks.DefaultEngine;
import com.pij.horrocks.Logger;
import com.pij.horrocks.MemoryStorage;
import com.pij.horrocks.Storage;
import com.pij.zworkout.FragmentScoped;
import com.pij.zworkout.R;
import com.pij.zworkout.service.api.StorageService;
import com.pij.zworkout.uc.WorkoutPersistenceUC;
import com.pij.zworkout.workout.viewmodel.CreateWorkoutFeature;
import com.pij.zworkout.workout.viewmodel.HorrocksWorkoutViewModel;
import com.pij.zworkout.workout.viewmodel.NameFeature;
import com.pij.zworkout.workout.viewmodel.SaveFeature;
import com.pij.zworkout.workout.viewmodel.StorageLoadingFeature;

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
                                              StorageLoadingFeature loadingFeature,
                                              SaveFeature saveFeature,
                                              CreateWorkoutFeature createWorkoutFeature) {
        return HorrocksWorkoutViewModel.create(logger,
                new DefaultEngine<>(logger),
                storage,
                new NameFeature(),
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
    StorageLoadingFeature provideStorageLoadingFeature(Logger logger, StorageService storage, Resources resources) {
        String defaultErrorMessage = resources.getString(R.string.list_loading_error_message);
        return new StorageLoadingFeature(logger, storage, defaultErrorMessage);
    }

    @Provides
    SaveFeature provideSaveFeature(Logger logger, WorkoutPersistenceUC storage, Storage<State> stateProvider, Resources resources) {
        String defaultErrorMessage = resources.getString(R.string.workout_save_error_message);
        return new SaveFeature(logger, storage, stateProvider::load, defaultErrorMessage);
    }

}