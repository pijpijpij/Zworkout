package com.pij.zworkout.uc;

import com.annimon.stream.Optional;
import com.pij.zworkout.service.api.WorkoutFile;

import java.io.File;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * <p>Created on 02/03/2018.</p>
 *
 * @author Pierrejean
 */

public interface WorkoutPersistenceUC {

    Observable<List<WorkoutFile>> workouts();

    Completable save(Workout data, Optional<File> file);
}
