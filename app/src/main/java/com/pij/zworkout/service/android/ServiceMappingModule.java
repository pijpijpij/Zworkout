package com.pij.zworkout.service.android;

import com.pij.zworkout.service.api.StorageService;
import com.pij.zworkout.service.api.WorkoutFile;

import java.io.OutputStream;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Uses the service module to provide service instance. These are fully working, threaded service implementations directly usable by th app
 * and it presenters and business layer.
 * <p>Created on 04/03/2018.</p>
 *
 * @author Pierrejean
 */
@Module(includes = ServiceModule.class)
public class ServiceMappingModule {

    @Provides
    @Reusable
    Threader provideAndroidThreader() {
        return new AndroidThreader(Schedulers.io(), AndroidSchedulers.mainThread());
    }

    @Provides
    StorageService provideStorageService(FolderStorageService functional, Threader threader) {
        return new StorageService() {
            @Override
            public Observable<List<WorkoutFile>> workouts() {
                return functional.workouts().compose(threader.forObservable());
            }

            @Override
            public Single<OutputStream> openForWrite(WorkoutFile file) {
                return functional.openForWrite(file).compose(threader.forSingle());
            }
        };
    }

}
