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

package com.pij.zworkout;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Log;

import com.pij.horrocks.Logger;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Provides system-level objects.
 */
@Module
abstract class ApplicationModule {

    @Provides
    static Logger provideLogger() {
        return new Logger() {
            @Override
            public void print(@NonNull Class<?> javaClass, @NonNull String message) {
                Log.d(javaClass.getSimpleName(), message);
            }

            @Override
            public void print(@NonNull Class<?> javaClass, @NonNull String message, @NonNull Throwable e) {
                Log.d(javaClass.getSimpleName(), message, e);

            }
        };
    }

    @Provides
    static Resources providesResources(Context context) {
        return context.getResources();
    }

    @Binds
    abstract Context bindContext(Application application);
}

