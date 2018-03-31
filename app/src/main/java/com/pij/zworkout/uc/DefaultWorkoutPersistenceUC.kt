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

package com.pij.zworkout.uc

import com.pij.zworkout.persistence.api.WorkoutSerializerService
import com.pij.zworkout.service.api.StorageService
import com.pij.zworkout.service.api.WorkoutFile
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import java.io.File

/**
 *
 * Created on 02/03/2018.
 *
 * @author Pierrejean
 */

internal class DefaultWorkoutPersistenceUC(private val storageService: StorageService,
                                           private val workoutSerializerService: WorkoutSerializerService,
                                           private val converter: PersistableWorkoutConverter) : WorkoutPersistenceUC {

    private val fileExtension = "zwo"
    private val reload = BehaviorSubject.createDefault(Any())

    override fun workouts(): Observable<List<WorkoutFile>> {
        return reload.flatMapSingle { _ -> storageService.workouts() }
    }

    override fun save(data: Workout, target: File?): Single<File> {
        return calculateFile(data, target)
                .flatMap { file ->
                    storageService.openForWrite(file)
                            .flatMapCompletable { output ->
                                Single.just(data)
                                        .map { converter.convert(it) }
                                        .flatMapCompletable { workoutSerializerService.write(it, output) }
                                        .doAfterTerminate { reload.onNext(Any()) }
                                        .doAfterTerminate { output.close() }
                            }
                            .andThen(Single.just(file))
                }
    }

    private fun calculateFile(data: Workout, file: File?): Single<File> {
        return when {
            file != null -> Single.just(file)
            else -> storageService.create("${data.name}.$fileExtension")
        }
    }

    override fun load(source: File): Single<Workout> {
        return Single.just(source)
                .flatMap {
                    storageService.openForRead(it)
                            .flatMap { input ->
                                workoutSerializerService.read(input)
                                        .map { converter.convert(it) }
                                        .doAfterTerminate { input.close() }
                            }
                }
    }
}