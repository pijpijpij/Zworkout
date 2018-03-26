package com.pij.zworkout.service.android

import io.reactivex.CompletableTransformer
import io.reactivex.ObservableTransformer
import io.reactivex.SingleTransformer

/**
 *
 * Created on 16/03/2018.
 *
 * @author Pierrejean
 */

internal interface Threader {

    fun <T> forObservable(): ObservableTransformer<T, T>

    fun <T> forSingle(): SingleTransformer<T, T>

    fun forCompletable(): CompletableTransformer
}
