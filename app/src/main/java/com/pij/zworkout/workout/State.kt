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
        val inProgress: Boolean = false,
        val showError: String? = null,
        val showSaved: Boolean = false,
        val workout: Workout = Workout(),
        val nameIsReadOnly: Boolean = false,
        val file: File? = null
)
