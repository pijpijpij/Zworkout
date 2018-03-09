package com.pij.zworkout.workout;


import android.content.res.Resources;

import com.pij.horrocks.DefaultEngine;
import com.pij.horrocks.Logger;
import com.pij.zworkout.FragmentScoped;
import com.pij.zworkout.R;
import com.pij.zworkout.service.api.StorageService;
import com.pij.zworkout.workout.viewmodel.CreateWorkoutFeature;
import com.pij.zworkout.workout.viewmodel.HorrocksViewModel;
import com.pij.zworkout.workout.viewmodel.NameFeature;
import com.pij.zworkout.workout.viewmodel.StorageLoadingFeature;

import dagger.Module;
import dagger.Provides;

/**
 * This is a Dagger module. We use this to pass in the View dependency to the {@link ViewModel}.
 */
@Module
public class WorkoutDetailModule {

    @FragmentScoped
    @Provides
    ViewModel provideHorrocksViewModel(Logger logger,
                                       StorageLoadingFeature loadingFeature,
                                       CreateWorkoutFeature createWorkoutFeature) {
        return HorrocksViewModel.create(logger,
                new DefaultEngine<>(logger),
                new NameFeature(),
                loadingFeature,
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


}