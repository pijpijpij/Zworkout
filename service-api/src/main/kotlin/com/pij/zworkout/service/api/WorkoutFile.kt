package com.pij.zworkout.service.api

import java.net.URI

/**
 *
 * Created on 02/03/2018.
 *
 * @author Pierrejean
 */
data class WorkoutFile(val uri: URI?, val name: String) {
    companion object {

        var UNDEFINED = WorkoutFile(null, "")

    }
}
