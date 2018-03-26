package com.pij.zworkout.uc

import com.annimon.stream.Optional
import com.pij.zworkout.persistence.api.WorkoutSerializerService
import com.pij.zworkout.service.api.StorageService
import com.pij.zworkout.service.api.WorkoutFile
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.Singles
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

    override fun save(data: Workout, target: Optional<File>): Completable {
        return Singles.zip(
                Single.just(data).map { converter.convert(it) },
                calculateFile(data, target).flatMap { storageService.openForWrite(it) })
                .flatMapCompletable { workoutSerializerService.write(it.first, it.second) }
    }

    private fun calculateFile(data: Workout, file: Optional<File>): Single<File> {
        return if (file.isPresent) Single.just(file.get())
        else storageService.create("${data.name()}.$fileExtension")
    }

    override fun load(source: File): Single<Workout> {
        return Single.just(source)
                .flatMap { storageService.openForRead(it) }
                .flatMap { workoutSerializerService.read(it) }
                .map { converter.convert(it) }
    }
}
