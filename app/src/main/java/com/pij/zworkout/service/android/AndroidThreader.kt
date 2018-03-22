package com.pij.zworkout.service.android

import io.reactivex.CompletableTransformer
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.SingleTransformer

/**
 *
 * Created on 16/03/2018.
 *
 * @author Pierrejean
 */

internal class AndroidThreader(private val background: Scheduler, private val foreground: Scheduler) : Threader {

    private val completableTransformer: CompletableTransformer by lazy {
        CompletableTransformer { it.subscribeOn(background).observeOn(foreground) }
    }

    override fun <T> forObservable(): ObservableTransformer<T, T> {
        return ObservableTransformer { it.subscribeOn(background).observeOn(foreground) }
    }

    override fun <T> forSingle(): SingleTransformer<T, T> {
        return SingleTransformer { it.subscribeOn(background).observeOn(foreground) }
    }

    override fun forCompletable(): CompletableTransformer {
        return completableTransformer
    }
}
