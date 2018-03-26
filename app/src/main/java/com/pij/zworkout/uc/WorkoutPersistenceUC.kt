package com.pij.zworkout.uc

import com.annimon.stream.Optional
import com.pij.zworkout.service.api.WorkoutFile
import io.reactivex.Observable
import io.reactivex.Single
import java.io.File

/**
 *
 * Created on 02/03/2018.
 *
 * @author Pierrejean
 */

interface WorkoutPersistenceUC {

    fun workouts(): Observable<List<WorkoutFile>>

    // TODO replace File by URI
    fun save(data: Workout, target: Optional<File>): Single<File>

    // TODO replace File by URI
    fun load(source: File): Single<Workout>
}
