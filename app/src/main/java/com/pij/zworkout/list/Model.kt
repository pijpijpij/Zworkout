package com.pij.zworkout.list

/**
 *
 * Created on 01/03/2018.
 *
 * @author Pierrejean
 */
data class Model(
        val inProgress: Boolean,
        val showError: String?,
        val showWorkout: WorkoutInfo?,
        val createWorkout: Boolean,
        val workouts: List<WorkoutInfo>
)