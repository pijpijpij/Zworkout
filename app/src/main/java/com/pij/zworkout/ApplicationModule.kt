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

package com.pij.zworkout

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.pij.android.utils.AndroidDebugLogger
import com.pij.utils.Logger
import dagger.Module
import dagger.Provides
import dagger.Reusable

/**
 * Provides system-level objects.
 */
@Module
internal class ApplicationModule {

    @Provides
    internal fun bindContext(application: Application): Context = application

    @Reusable
    @Provides
    internal fun provideAndroidDebugLogger(): Logger {
        return AndroidDebugLogger()
    }

    @Provides
    fun providesResources(context: Context): Resources {
        return context.resources
    }
}

