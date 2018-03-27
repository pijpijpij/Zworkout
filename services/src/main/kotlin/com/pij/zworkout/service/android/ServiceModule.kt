package com.pij.zworkout.service.android

import android.content.Context

import com.pij.utils.Logger

import dagger.Module
import dagger.Provides

/**
 * Provides concrete objects that implement services defines in the service-api module.
 *
 * Created on 04/03/2018.
 *
 * @author Pierrejean
 */
@Module
class ServiceModule {

    @Provides
    internal fun provideFolderStorageService(context: Context, logger: Logger): FolderStorageService {
        return FolderStorageService(context.filesDir, logger)
    }

}
