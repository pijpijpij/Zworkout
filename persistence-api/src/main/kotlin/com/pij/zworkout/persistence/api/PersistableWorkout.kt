/*
 * Copyright (c) 2018, Chiswick Forest
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

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
        @field:Element(required = false) var description: String? = null,
        @field:Element(required = false) var sportType: SportType? = null)

data class EmptyString(

        @field:Text(required = false) var value: String? = null
)

enum class SportType {
    BIKE
}
