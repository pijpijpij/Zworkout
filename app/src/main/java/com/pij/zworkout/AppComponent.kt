package com.pij.zworkout

import android.app.Application
import com.pij.zworkout.persistence.xml.PersistenceModule
import com.pij.zworkout.service.android.ServiceMappingModule
import com.pij.zworkout.uc.UCModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ApplicationModule::class,
    ActivityBindingModule::class,
    UCModule::class,
    ServiceMappingModule::class,
    PersistenceModule::class,
    AndroidSupportInjectionModule::class])
interface AppComponent : AndroidInjector<ZworkoutApplication> {

    override fun inject(application: ZworkoutApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): AppComponent.Builder

        fun build(): AppComponent
    }
}
