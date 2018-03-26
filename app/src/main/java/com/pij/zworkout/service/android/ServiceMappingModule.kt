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
            override fun workouts() = functional.workouts().compose(threader.forObservable())

            override fun create(name: String) = functional.create(name).compose(threader.forSingle())

            override fun openForWrite(file: File) = functional.openForWrite(file).compose(threader.forSingle())
        }
    }

}
