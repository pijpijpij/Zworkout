package com.pij.zworkout.list;


import android.content.res.Resources;

import com.pij.horrocks.DefaultEngine;
import com.pij.horrocks.Logger;
import com.pij.zworkout.ActivityScoped;
import com.pij.zworkout.R;
import com.pij.zworkout.list.viewmodel.CreateWorkoutFeature;
import com.pij.zworkout.list.viewmodel.HorrocksWorkoutsViewModel;
import com.pij.zworkout.list.viewmodel.ShowDetailFeature;
import com.pij.zworkout.list.viewmodel.StorageLoadingFeature;
import com.pij.zworkout.service.api.StorageService;

import dagger.Module;
import dagger.Provides;

/**
 * This is a Dagger module. We use this to pass in the View dependency to the {@link WorkoutsViewModel}.
 */
@Module
public class WorkoutsModule {

    @ActivityScoped
    @Provides
    WorkoutsViewModel provideWorkoutsViewModel(Logger logger, StorageLoadingFeature loadingFeature) {
        return HorrocksWorkoutsViewModel.create(logger,
                new DefaultEngine<>(logger),
                loadingFeature,
                new ShowDetailFeature(),
                new CreateWorkoutFeature());
    }

    @ActivityScoped
    @Provides
    StorageLoadingFeature provideStorageLoadingFeature(Logger logger, StorageService storage, Resources resources) {
        String defaultErrorMessage = resources.getString(R.string.list_loading_error_message);
        return new StorageLoadingFeature(logger, storage, defaultErrorMessage);
    }


    @ActivityScoped
    @Provides
    Adapter provideAdapter(WorkoutsViewModel viewModel) {
        return new Adapter(viewModel::select);
    }

}