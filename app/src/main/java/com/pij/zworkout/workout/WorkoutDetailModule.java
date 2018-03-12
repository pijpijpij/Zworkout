package com.pij.zworkout.workout;


import android.content.res.Resources;

import com.pij.horrocks.DefaultEngine;
import com.pij.horrocks.Logger;
import com.pij.zworkout.FragmentScoped;
import com.pij.zworkout.R;
import com.pij.zworkout.service.api.StorageService;
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
                                              StorageLoadingFeature loadingFeature,
                                              SaveFeature saveFeature,
                                              CreateWorkoutFeature createWorkoutFeature) {
        return HorrocksWorkoutViewModel.create(logger,
                new DefaultEngine<>(logger),
                new NameFeature(),
                loadingFeature,
                saveFeature,
                createWorkoutFeature
        );
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
    SaveFeature provideSaveFeature(Logger logger, StorageService storage, Resources resources) {
        String defaultErrorMessage = resources.getString(R.string.workout_save_error_message);
        return new SaveFeature(logger, storage, defaultErrorMessage);
    }

}