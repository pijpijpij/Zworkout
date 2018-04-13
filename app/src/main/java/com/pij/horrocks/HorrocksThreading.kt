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

package com.pij.horrocks

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * <p>Created on 09/04/2018.</p>
 * @author Pierrejean
 */

class BackgroundCreator<E, S>(
        private val decorated: ReducerCreator<E, S>,
        private val background: Scheduler,
        private val foreground: Scheduler
) : ReducerCreator<E, S> {
    override fun trigger(event: E) {
        decorated.trigger(event)
    }

    override fun reducers(): Observable<out Reducer<S>> = decorated.reducers()
//            .doOnNext { println("PJC working on this thread") }
//            .doOnSubscribe { println("PJC subscribing on this thread") }
//            .subscribeOn(background)
//            .observeOn(foreground)
//            .doOnSubscribe { println("PJC observing on this thread") }
//            .doOnNext { println("PJC emitting on this thread") }
}

fun <E, S> androidCreator(decorated: ReducerCreator<E, S>) =
        BackgroundCreator(decorated, Schedulers.io(), AndroidSchedulers.mainThread())

