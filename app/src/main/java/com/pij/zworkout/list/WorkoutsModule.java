package com.pij.zworkout.list;


import com.pij.horrocks.DefaultEngine;
import com.pij.horrocks.Logger;
import com.pij.zworkout.ActivityScoped;
import com.pij.zworkout.list.viewmodel.DummyLoadingFeature;
import com.pij.zworkout.list.viewmodel.HorrocksViewModel;

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
        return HorrocksViewModel.create(logger, new DefaultEngine<>(logger), new DummyLoadingFeature());
    }

}
