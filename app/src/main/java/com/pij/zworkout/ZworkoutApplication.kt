package com.pij.zworkout

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

/**
 *
 * Created on 01/03/2018.
 *
 * @author Pierrejean
 */

class ZworkoutApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent = DaggerAppComponent.builder().application(this).build()
        appComponent.inject(this)
        return appComponent
    }
}
