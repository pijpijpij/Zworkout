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

package com.pij

import android.support.annotation.CheckResult

/**
 * Copies the list and replaces its <code>position</code> item with the one provided.
 * It cannot be an operator as the result is the new list.
 * @return <b>not</b> the previous element but the new list.
 */
@CheckResult
fun <T> List<T>.set(position: Int, item: T): List<T> {
    val mutableList = toMutableList()
    mutableList[position] = item
    return mutableList.toList()
}

/**
 * Copies the list and adds the item at <code>position</code>.
 * It cannot be an operator as the result is the new list.
 * @return <b>not</b> a boolean but the new list
 */
@CheckResult
fun <T> List<T>.add(position: Int, item: T): List<T> {
    val mutableList = toMutableList()
    mutableList.add(position, item)
    return mutableList.toList()
}

