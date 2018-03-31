/*
 * Copyright (c) 2018, Chiswick Forest
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

package com.pij.zworkout.list


import android.content.res.Resources
import com.pij.horrocks.DefaultEngine
import com.pij.utils.Logger
import com.pij.zworkout.ActivityScoped
import com.pij.zworkout.R
import com.pij.zworkout.list.feature.CreateWorkoutFeature
import com.pij.zworkout.list.feature.ShowDetailFeature
import com.pij.zworkout.list.feature.StorageLoadingFeature
import com.pij.zworkout.uc.WorkoutPersistenceUC
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
    internal fun provideStorageLoadingFeature(logger: Logger, storage: WorkoutPersistenceUC, resources: Resources): StorageLoadingFeature {
        val defaultErrorMessage = resources.getString(R.string.list_loading_error_message)
        return StorageLoadingFeature(logger, storage, defaultErrorMessage)
    }


    @ActivityScoped
    @Provides
    internal fun provideAdapter(viewModel: WorkoutsViewModel): Adapter {
        return Adapter { workout -> viewModel.select(workout) }
    }

}