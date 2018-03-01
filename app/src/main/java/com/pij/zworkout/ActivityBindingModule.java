package com.pij.zworkout;

import com.pij.zworkout.list.WorkoutsActivity;
import com.pij.zworkout.list.WorkoutsModule;
import com.pij.zworkout.workout.WorkoutDetailActivity;
import com.pij.zworkout.workout.WorkoutDetailFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = WorkoutsModule.class)
    abstract WorkoutsActivity workoutsActivity();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract WorkoutDetailFragment workoutDetailFragment();

    @ActivityScoped
    @ContributesAndroidInjector(/*modules = WorkoutDetailModule.class*/)
    abstract WorkoutDetailActivity taskDetailActivity();
}
