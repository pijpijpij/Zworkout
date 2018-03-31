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

package com.pij.zworkout.service.android

import com.pij.zworkout.service.api.StorageService
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File


/**
 * Uses the service module to provide service instance. These are fully working, threaded service implementations directly usable by th app
 * and it presenters and business layer.
 *
 * Created on 04/03/2018.
 *
 * @author Pierrejean
 */
@Module(includes = [(ServiceModule::class)])
class ServiceMappingModule {

    @Provides
    @Reusable
    internal fun provideAndroidThreader(): Threader {
        return AndroidThreader(Schedulers.io(), AndroidSchedulers.mainThread())
    }

    @Provides
    internal fun provideStorageService(functional: FolderStorageService, threader: Threader): StorageService {
        return object : StorageService {
            override fun workouts() = functional.workouts().compose(threader.forSingle())

            override fun create(name: String) = functional.create(name).compose(threader.forSingle())

            override fun openForWrite(target: File) = functional.openForWrite(target).compose(threader.forSingle())

            override fun openForRead(source: File) = functional.openForRead(source).compose(threader.forSingle())
        }
    }

}
