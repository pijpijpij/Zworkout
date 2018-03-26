package com.pij.zworkout.persistence.xml

import com.pij.zworkout.persistence.api.PersistableWorkout
import com.pij.zworkout.persistence.api.WorkoutSerializerService
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.rxkotlin.toCompletable
import org.simpleframework.xml.core.Persister
import java.io.InputStream
import java.io.OutputStream

/**
 *
 * Created on 16/03/2018.
 *
 * @author Pierrejean
 */

internal class XmlWorkoutSerializerService(private val serializer: Persister) : WorkoutSerializerService {

    override fun write(data: PersistableWorkout, target: OutputStream): Completable {
        return { serializer.write(data, target) }.toCompletable()
    }

    override fun read(source: InputStream): Single<PersistableWorkout> {
        return Single.just(source).map { serializer.read(PersistableWorkout(), it) }
    }

}
