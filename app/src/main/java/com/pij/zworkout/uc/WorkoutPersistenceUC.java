package com.pij.zworkout.uc;

import com.annimon.stream.Optional;
import com.pij.zworkout.service.api.WorkoutFile;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * <p>Created on 02/03/2018.</p>
 *
 * @author Pierrejean
 */

public interface WorkoutPersistenceUC {

    Observable<List<WorkoutFile>> workouts();

    // TODO replace File by URI
    Single<File> save(Workout data, Optional<File> file);

    // TODO replace File by URI
    Single<Workout> load(File source);
}
