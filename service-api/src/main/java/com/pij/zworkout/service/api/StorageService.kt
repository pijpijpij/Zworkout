package com.pij.zworkout.service.api

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.annotations.CheckReturnValue
import java.io.File
import java.io.InputStream
import java.io.OutputStream

/**
 *
 * Created on 02/03/2018.
 *
 * @author Pierrejean
 */

interface StorageService {

    @CheckReturnValue
    fun workouts(): Observable<List<WorkoutFile>>

    @CheckReturnValue
    fun create(name: String): Single<File>

    @CheckReturnValue
    fun openForWrite(target: File): Single<OutputStream>

    @CheckReturnValue
    fun openForRead(source: File): Single<InputStream>
}
