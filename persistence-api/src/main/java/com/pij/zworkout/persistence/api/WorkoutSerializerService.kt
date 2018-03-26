package com.pij.zworkout.persistence.api

import io.reactivex.Completable
import io.reactivex.Single
import java.io.InputStream
import java.io.OutputStream

/**
 *
 * Created on 12/03/2018.
 *
 * @author Pierrejean
 */

interface WorkoutSerializerService {

    fun write(data: PersistableWorkout, target: OutputStream): Completable

    fun read(source: InputStream): Single<PersistableWorkout>
}
