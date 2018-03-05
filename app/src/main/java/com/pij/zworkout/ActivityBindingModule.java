package com.pij.zworkout;

import com.pij.zworkout.list.WorkoutsActivity;
import com.pij.zworkout.list.WorkoutsModule;
import com.pij.zworkout.workout.WorkoutDetailActivity;
import com.pij.zworkout.workout.WorkoutDetailFragment;
import com.pij.zworkout.workout.WorkoutDetailModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = WorkoutsModule.class)
    abstract WorkoutsActivity workoutsActivity();

    @FragmentScoped
    @ContributesAndroidInjector(modules = WorkoutDetailModule.class)
    abstract WorkoutDetailFragment workoutDetailFragment();

    @ActivityScoped
    @ContributesAndroidInjector()
    abstract WorkoutDetailActivity taskDetailActivity();
}
