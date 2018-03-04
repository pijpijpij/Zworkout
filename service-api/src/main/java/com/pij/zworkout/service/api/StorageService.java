package com.pij.zworkout.service.api;

import java.util.List;

import io.reactivex.Observable;

/**
 * <p>Created on 02/03/2018.</p>
 *
 * @author Pierrejean
 */

public interface StorageService {

    Observable<List<WorkoutFile>> workouts();
}
