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

package com.pij.zworkout.workout


import android.content.res.Resources
import com.pij.horrocks.DefaultEngine
import com.pij.horrocks.MemoryStorage
import com.pij.horrocks.StateProvider
import com.pij.horrocks.Storage
import com.pij.utils.Logger
import com.pij.zworkout.FragmentScoped
import com.pij.zworkout.R
import com.pij.zworkout.uc.WorkoutPersistenceUC
import com.pij.zworkout.workout.feature.*
import dagger.Module
import dagger.Provides
import javax.inject.Provider

/**
 * This is a Dagger module. We use this to pass in the View dependency to the [WorkoutViewModel].
 */
@Module
class WorkoutDetailModule {

    @Provides
    internal fun provideEffortsAdapter(): EffortsAdapter {
        return EffortsAdapter()
    }

    @FragmentScoped
    @Provides
    internal fun provideHorrocksViewModel(logger: Logger,
                                          storage: Storage<State>,
                                          loadingFeature: LoadFeature,
                                          saveFeature: SaveFeature,
                                          createWorkoutFeature: CreateWorkoutFeature): WorkoutViewModel {
        return HorrocksWorkoutViewModel.create(logger,
                DefaultEngine(logger),
                storage,
                NameFeature(),
                DescriptionFeature(),
                loadingFeature,
                saveFeature,
                createWorkoutFeature
        )
    }


    @FragmentScoped
    @Provides
    internal fun provideMemoryStorage(): Storage<State> {
        return MemoryStorage(HorrocksWorkoutViewModel.initialState())
    }

    @Provides
    internal fun provideCreateWorkoutFeature(): CreateWorkoutFeature {
        // TODO use a real name calculator
        return CreateWorkoutFeature(Provider { "Unnamed workout" })
    }

    @Provides
    internal fun provideStorageLoadingFeature(logger: Logger, storage: WorkoutPersistenceUC, resources: Resources): LoadFeature {
        val defaultErrorMessage = resources.getString(R.string.list_loading_error_message)
        return LoadFeature(logger, storage, defaultErrorMessage)
    }

    @Provides
    internal fun provideSaveFeature(logger: Logger, storage: WorkoutPersistenceUC, stateProvider: Storage<State>, resources: Resources): SaveFeature {
        val defaultErrorMessage = resources.getString(R.string.workout_save_error_message)
        return SaveFeature(logger, storage, StateProvider { stateProvider.load() }, defaultErrorMessage)
    }

}