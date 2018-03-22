package com.pij.zworkout.persistence.xml;

import com.pij.zworkout.persistence.api.WorkoutSerializerService;

import dagger.Module;
import dagger.Provides;

/**
 * <p>Created on 04/03/2018.</p>
 *
 * @author Pierrejean
 */
@Module
public class PersistenceModule {

//    @Provides
//    WorkoutSerializerService provideDummyWorkoutSerializerService() {
//        return new DummyWorkoutSerializerService();
//    }

    @Provides
    WorkoutSerializerService provideXmlWorkoutSerializerService() {
        return new XmlWorkoutSerializerService();
    }
}
