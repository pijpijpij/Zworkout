package com.pij.zworkout.service.api;

import java.io.OutputStream;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.CheckReturnValue;

/**
 * <p>Created on 02/03/2018.</p>
 *
 * @author Pierrejean
 */

public interface StorageService {

    @CheckReturnValue
    Observable<List<WorkoutFile>> workouts();

    @CheckReturnValue
    Single<OutputStream> openForWrite(WorkoutFile file);
}
