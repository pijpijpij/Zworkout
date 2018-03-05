package com.pij.zworkout.workout;


import android.content.res.Resources;

import com.pij.horrocks.DefaultEngine;
import com.pij.horrocks.Logger;
import com.pij.zworkout.ActivityScoped;
import com.pij.zworkout.R;
import com.pij.zworkout.service.api.StorageService;
import com.pij.zworkout.workout.viewmodel.CreateWorkoutFeature;
import com.pij.zworkout.workout.viewmodel.HorrocksViewModel;
import com.pij.zworkout.workout.viewmodel.StorageLoadingFeature;

import dagger.Module;
import dagger.Provides;

/**
 * This is a Dagger module. We use this to pass in the View dependency to the {@link ViewModel}.
 */
@Module
class WorkoutDetailModule {

    @ActivityScoped
    @Provides
    ViewModel provideHorrocksViewModel(Logger logger, StorageLoadingFeature loadingFeature) {
        return HorrocksViewModel.create(logger,
                new DefaultEngine<>(logger),
                loadingFeature,
                new CreateWorkoutFeature());
    }

    @ActivityScoped
    @Provides
    StorageLoadingFeature provideStorageLoadingFeature(Logger logger, StorageService storage, Resources resources) {
        String defaultErrorMessage = resources.getString(R.string.list_loading_error_message);
        return new StorageLoadingFeature(logger, storage, defaultErrorMessage);
    }


}