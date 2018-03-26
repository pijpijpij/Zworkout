package com.pij.zworkout.workout

import com.pij.zworkout.uc.Workout
import java.io.File

/**
 *
 * Created on 01/03/2018.
 *
 * @author Pierrejean
 */
data class State(
        val inProgress: Boolean,
        val showError: String?,
        val showSaved: Boolean,
        val workout: Workout,
        val nameIsReadOnly: Boolean,
        val file: File?
)
