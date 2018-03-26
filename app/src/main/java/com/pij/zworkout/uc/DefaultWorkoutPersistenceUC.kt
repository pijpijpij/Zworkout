package com.pij.zworkout.uc

import com.pij.zworkout.persistence.api.WorkoutSerializerService
import com.pij.zworkout.service.api.StorageService
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

internal class DefaultWorkoutPersistenceUC(private val storageService: StorageService,
                                           private val workoutSerializerService: WorkoutSerializerService,
                                           private val converter: PersistableWorkoutConverter) : WorkoutPersistenceUC {

    private val fileExtension = "zwo"

    override fun workouts(): Observable<List<WorkoutFile>> {
        return storageService.workouts()
    }

    override fun save(data: Workout, target: File?): Single<File> {
        return calculateFile(data, target)
                .flatMap { file ->
                    storageService.openForWrite(file)
                            .flatMapCompletable { output ->
                                Single.just(data)
                                        .map { converter.convert(it) }
                                        .flatMapCompletable { workoutSerializerService.write(it, output) }
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