package com.pij.zworkout.uc;

import com.pij.zworkout.persistence.api.WorkoutSerializerService;
import com.pij.zworkout.service.api.StorageService;
import com.pij.zworkout.service.api.WorkoutFile;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * <p>Created on 02/03/2018.</p>
 *
 * @author Pierrejean
 */

public class DefaultWorkoutPersistenceUC implements WorkoutPersistenceUC {

    private final StorageService storageService;
    private final WorkoutSerializerService workoutSerializerService;
    private final PersistableWorkoutConverter converter;

    public DefaultWorkoutPersistenceUC(StorageService storageService,
                                       WorkoutSerializerService workoutSerializerService,
                                       PersistableWorkoutConverter converter) {
        this.storageService = storageService;
        this.workoutSerializerService = workoutSerializerService;
        this.converter = converter;
    }

    @Override
    public Observable<List<WorkoutFile>> workouts() {
        return storageService.workouts();
    }

    @Override
    public Completable save(Workout data, WorkoutFile file) {
        return Single.zip(
                Single.just(data).map(converter::convert),
                storageService.openForWrite(file),
                workoutSerializerService::write)
                .flatMapCompletable(serialisation -> serialisation);
    }

}
