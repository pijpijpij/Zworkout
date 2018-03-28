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

package com.pij.zworkout.persistence.xml

import com.pij.zworkout.persistence.api.SportType
import org.simpleframework.xml.core.Persister
import org.simpleframework.xml.transform.Matcher
import org.simpleframework.xml.transform.Transform


internal fun workoutPersister(): Persister {
    val transform = SportTypeTransform()
    val matcher = Matcher { type -> if (type == SportType::class.java) transform else null }
    return Persister(matcher)
}


/**
 * @author Pierrejean
 */
internal class SportTypeTransform : Transform<SportType> {

    override fun write(value: SportType): String = value.name.toLowerCase()

    override fun read(value: String): SportType = SportType.valueOf(value.toUpperCase())
}
