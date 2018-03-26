package com.pij.zworkout.list

import io.reactivex.Observable

/**
 *
 * Created on 01/03/2018.
 *
 * @author Pierrejean
 */

internal interface WorkoutsViewModel {

    fun load()

    fun select(workout: WorkoutInfo)

    fun createWorkout()

    fun model(): Observable<Model>
}
