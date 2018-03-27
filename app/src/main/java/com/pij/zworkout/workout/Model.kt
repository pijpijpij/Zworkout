package com.pij.zworkout.workout

/**
 *
 * Created on 01/03/2018.
 *
 * @author Pierrejean
 */
data class Model(
        val inProgress: Boolean,
        val showError: String?,
        val showSaved: Boolean,
        val name: String,
        val nameIsReadOnly: Boolean,
        val description: String
)