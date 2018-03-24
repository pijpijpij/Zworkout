package com.pij.zworkout.persistence.xml

import com.pij.zworkout.persistence.api.PersistableWorkout
import com.pij.zworkout.persistence.api.WorkoutSerializerService
import io.reactivex.Completable
import java.io.OutputStream

/**
 *
 * Created on 13/03/2018.
 *
 * @author Pierrejean
 */
internal class DummyWorkoutSerializerService : WorkoutSerializerService {

    override fun write(data: PersistableWorkout, target: OutputStream): Completable {
        return Completable.error(UnsupportedOperationException("write([data, target]) not implemented."))
    }

}
