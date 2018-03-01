package com.pij.zworkout;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

/**
 * <p>Created on 01/03/2018.</p>
 *
 * @author Pierrejean
 */

public class ZworkoutApplication extends DaggerApplication {
    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        AppComponent appComponent = DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);
        return appComponent;
    }
}
