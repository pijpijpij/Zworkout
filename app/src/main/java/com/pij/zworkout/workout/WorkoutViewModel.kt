package com.pij.zworkout.workout

import io.reactivex.Observable

/**
 *
 * Created on 01/03/2018.
 *
 * @author Pierrejean
 */

internal interface WorkoutViewModel {

    fun model(): Observable<Model>

    fun load(itemId: String)

    fun createWorkout()

    fun name(newValue: String)

    fun description(newValue: String)

    fun save()
}
