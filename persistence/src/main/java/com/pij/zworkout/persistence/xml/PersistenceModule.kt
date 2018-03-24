package com.pij.zworkout.persistence.xml

import com.pij.zworkout.persistence.api.WorkoutSerializerService

import dagger.Module
import dagger.Provides
import org.simpleframework.xml.core.Persister

/**
 *
 * Created on 04/03/2018.
 *
 * @author Pierrejean
 */
@Module
class PersistenceModule {

    //    @Provides
    //    WorkoutSerializerService provideDummyWorkoutSerializerService() {
    //        return new DummyWorkoutSerializerService();
    //    }

    @Provides
    internal fun provideXmlWorkoutSerializerService(): WorkoutSerializerService {
        return XmlWorkoutSerializerService(Persister())
    }
}
