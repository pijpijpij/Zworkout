package com.pij.zworkout.service.android;

import android.content.Context;

import com.pij.zworkout.service.api.WorkoutSerializerService;

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
    FolderStorageService provideFolderStorageService(Context context, WorkoutSerializerService serialiser) {
        return new FolderStorageService(context.getFilesDir(), serialiser);
    }

    @Provides
    DummyWorkoutSerializerService provideDummyWorkoutSerializerService() {
        return new DummyWorkoutSerializerService();
    }

    //    @Provides
//    WorkoutSerializerService provideXmlWorkoutSerializerService() {
//        return new XmlWorkoutSerializerService();
//    }
}
