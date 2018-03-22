package com.pij.zworkout.service.android;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Provides concrete objects that implement services defines in the service-api module.
 * <p>Created on 04/03/2018.</p>
 *
 * @author Pierrejean
 */
@Module
public class ServiceModule {

    @Provides
    FolderStorageService provideFolderStorageService(Context context) {
        return new FolderStorageService(context.getFilesDir());
    }

}
