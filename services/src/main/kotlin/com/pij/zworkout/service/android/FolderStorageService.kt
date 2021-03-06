/*
 * Copyright (c) 2018, Chiswick Forest
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

package com.pij.zworkout.service.android

import com.pij.utils.Logger
import com.pij.zworkout.service.api.StorageService
import com.pij.zworkout.service.api.WorkoutFile
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.util.regex.Pattern

/**
 *
 * Created on 02/03/2018.
 *
 * @author Pierrejean
 */

class FolderStorageService(private val root: File, private val logger: Logger) : StorageService {
    private val fileMatcher = Pattern.compile("(.*)\\.zwo$", Pattern.CASE_INSENSITIVE)

    // TODO pass the pattern as argument to #workouts() to move all extension notion ot the same place
    override fun workouts(): Single<List<WorkoutFile>> {
        return Observable.defer { Observable.fromArray(*root.listFiles()) }
                .flatMapMaybe { convert(it) }
                .toList()
    }

    override fun create(name: String): Single<File> {
        return Single.just(name)
                .map { defaultFile(it) }
                .doOnSuccess { checkDoesNotExist(it) }
    }

    override fun openForWrite(target: File): Single<OutputStream> {
        return Single.just(target)
                .doOnSuccess { checkIsInRoot(it) }
                .map { it.outputStream() }
                .cast(OutputStream::class.java)
                .doOnError { logger.print(javaClass, it, "") }
    }

    override fun openForRead(source: File): Single<InputStream> {
        return Single.just(source)
                .doOnSuccess { checkIsInRoot(it) }
                .map { it.inputStream() }
                .cast(InputStream::class.java)
                .doOnError { logger.print(javaClass, it, "") }
    }

    private fun defaultFile(name: String): File = File(root, name)

    private fun checkDoesNotExist(file: File) {
        if (file.exists()) throw IllegalArgumentException("File $file already exists")
    }

    private fun checkIsInRoot(it: File) {
        if (!it.startsWith(root)) throw IllegalArgumentException("This file is not in the root: $it and $root.")
    }


    private fun convert(file: File): Maybe<WorkoutFile> {
        val name: Maybe<String> = Maybe.just(file.name)
                .map { fileMatcher.matcher(it) }
                .onErrorComplete()
                .filter { it.matches() }
                .map { matcher -> matcher.group(1) }
        return Maybe.zip(name, Maybe.just(file), BiFunction(this::workoutFile))
    }

    private fun workoutFile(workoutName: String, file: File): WorkoutFile {
        return WorkoutFile(file.toURI(), workoutName)
    }

}
