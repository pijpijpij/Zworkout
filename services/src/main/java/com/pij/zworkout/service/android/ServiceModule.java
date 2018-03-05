package com.pij.zworkout.service.android;

import com.pij.zworkout.service.api.StorageService;

import dagger.Module;
import dagger.Provides;

/**
 * <p>Created on 04/03/2018.</p>
 *
 * @author Pierrejean
 */
@Module(includes = FunctionalServiceModule.class)
public class ServiceModule {

    @Provides
    StorageService provideStorageService(FolderStorageService functional) {
        return functional;
    }
}