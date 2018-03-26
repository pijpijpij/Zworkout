package com.pij.zworkout

import com.pij.zworkout.list.WorkoutsActivity
import com.pij.zworkout.list.WorkoutsModule
import com.pij.zworkout.workout.WorkoutDetailActivity
import com.pij.zworkout.workout.WorkoutDetailFragment
import com.pij.zworkout.workout.WorkoutDetailModule

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = arrayOf(WorkoutsModule::class))
    internal abstract fun workoutsActivity(): WorkoutsActivity

    @FragmentScoped
    @ContributesAndroidInjector(modules = arrayOf(WorkoutDetailModule::class))
    internal abstract fun workoutDetailFragment(): WorkoutDetailFragment

    @ActivityScoped
    @ContributesAndroidInjector
    internal abstract fun taskDetailActivity(): WorkoutDetailActivity
}
