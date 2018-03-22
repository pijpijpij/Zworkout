/*
 * Copyright 2018, Chiswick Forest
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.pij.zworkout.uc;

import com.pij.zworkout.persistence.api.WorkoutSerializerService;
import com.pij.zworkout.service.api.StorageService;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;

/**
 * Provides functional objects
 */
@Module
public abstract class UCModule {

    @Reusable
    @Provides
    static WorkoutPersistenceUC provideDefaultWorkoutPersistenceUC(StorageService storageService,
                                                                   WorkoutSerializerService workoutSerializerService) {
        return new DefaultWorkoutPersistenceUC(storageService, workoutSerializerService, new PersistableWorkoutConverter());
    }

}

