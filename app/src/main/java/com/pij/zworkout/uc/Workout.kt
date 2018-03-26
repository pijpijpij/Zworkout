package com.pij.zworkout.uc

/**
 *
 * Created on 12/03/2018.
 *
 * @author Pierrejean
 */
data class Workout(val name: String, val description: String) {

//    abstract fun copy(: Builder
//
//    fun name(name: String): Workout {
//        return copy(.name(name).build()
//    }
//
//    @AutoValue.Builder
//    abstract class Builder {
//        abstract fun name(name: String): Builder
//
//        abstract fun description(description: String): Builder
//
//        abstract fun build(): Workout
//    }

    companion object {

        val EMPTY = Workout(name = "", description = "")
    }
}
