package com.pij.zworkout.service.android;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * <p>Created on 04/03/2018.</p>
 *
 * @author Pierrejean
 */
@Module
public class FunctionalServiceModule {

    @Provides
    FolderStorageService provideFolderStorageService(Context context) {
        return new FolderStorageService(context.getFilesDir());
    }
}
