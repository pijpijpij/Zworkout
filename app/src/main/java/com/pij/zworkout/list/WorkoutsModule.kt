package com.pij.zworkout.list


import android.content.res.Resources

import com.pij.horrocks.DefaultEngine
import com.pij.utils.Logger
import com.pij.zworkout.ActivityScoped
import com.pij.zworkout.R
import com.pij.zworkout.list.viewmodel.CreateWorkoutFeature
import com.pij.zworkout.list.viewmodel.HorrocksWorkoutsViewModel
import com.pij.zworkout.list.viewmodel.ShowDetailFeature
import com.pij.zworkout.list.viewmodel.StorageLoadingFeature
import com.pij.zworkout.service.api.StorageService

import dagger.Module
import dagger.Provides

/**
 * This is a Dagger module. We use this to pass in the View dependency to the [WorkoutsViewModel].
 */
@Module
class WorkoutsModule {

    @ActivityScoped
    @Provides
    internal fun provideWorkoutsViewModel(logger: Logger, loadingFeature: StorageLoadingFeature): WorkoutsViewModel {
        return HorrocksWorkoutsViewModel.create(logger,
                DefaultEngine(logger),
                loadingFeature,
                ShowDetailFeature(),
                CreateWorkoutFeature())
    }

    @ActivityScoped
    @Provides
    internal fun provideStorageLoadingFeature(logger: Logger, storage: StorageService, resources: Resources): StorageLoadingFeature {
        val defaultErrorMessage = resources.getString(R.string.list_loading_error_message)
        return StorageLoadingFeature(logger, storage, defaultErrorMessage)
    }


    @ActivityScoped
    @Provides
    internal fun provideAdapter(viewModel: WorkoutsViewModel): Adapter {
        return Adapter { workout -> viewModel.select(workout) }
    }

}