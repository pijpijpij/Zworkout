package com.pij.zworkout.uc;

import com.annimon.stream.Optional;
import com.pij.zworkout.persistence.api.WorkoutSerializerService;
import com.pij.zworkout.service.api.StorageService;
import com.pij.zworkout.service.api.WorkoutFile;

import java.io.File;
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

    private final static String FILE_EXTENSION = ".zwo";

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
    public Completable save(Workout data, Optional<File> file) {
        return Single.defer(() -> Single.zip(
                Single.just(data).map(converter::convert),
                calculateFile(data, file).flatMap(storageService::open),
                workoutSerializerService::write))
                .flatMapCompletable(serialisation -> serialisation);
    }

    private Single<File> calculateFile(Workout data, Optional<File> file) {
        return file.isPresent() ? Single.just(file.get()) : storageService.create(data.name() + FILE_EXTENSION);
    }
}
