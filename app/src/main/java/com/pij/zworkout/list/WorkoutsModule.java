package com.pij.zworkout.list;


import com.pij.horrocks.Logger;
import com.pij.zworkout.ActivityScoped;

import dagger.Module;
import dagger.Provides;

/**
 * This is a Dagger module. We use this to pass in the View dependency to the {@link ViewModel}.
 */
@Module
public class WorkoutsModule {

    @ActivityScoped
    @Provides
    ViewModel provideWorkoutsViewModel(Logger logger) {
        throw new UnsupportedOperationException("provideWorkoutsViewModel([logger]) not implemented yet");
//        return new FeaturedViewModel(logger, new DefaultEngine<>(logger));
    }

}
