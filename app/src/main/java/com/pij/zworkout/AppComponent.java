package com.pij.zworkout;

import android.app.Application;

import com.pij.zworkout.persistence.xml.PersistenceModule;
import com.pij.zworkout.service.android.ServiceMappingModule;
import com.pij.zworkout.uc.UCModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        ActivityBindingModule.class,
        UCModule.class,
        ServiceMappingModule.class,
        PersistenceModule.class,
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
