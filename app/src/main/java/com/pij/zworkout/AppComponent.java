package com.pij.zworkout;

import android.app.Application;

import com.pij.zworkout.service.android.ServiceModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        ActivityBindingModule.class,
        ServiceModule.class,
        AndroidSupportInjectionModule.class
})

public interface AppComponent extends AndroidInjector<ZworkoutApplication> {

    @Override
    void inject(ZworkoutApplication application);

    @Component.Builder
    interface Builder {

        @BindsInstance
        AppComponent.Builder application(Application application);

        AppComponent build();
    }
}
