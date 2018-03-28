package com.pij.zworkout.persistence.api

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import org.simpleframework.xml.Text

/**
 *
 * Created on 12/03/2018.
 *
 * @author Pierrejean
 */
@Root(name = "workout_file")
data class PersistableWorkout(

        @field:Element var name: EmptyString = EmptyString(),
        @field:Element(required = false) var description: String? = null)

data class EmptyString(

        @field:Text(required = false) var value: String? = null
)

