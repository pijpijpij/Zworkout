package com.pij.zworkout.service.android;

import android.support.annotation.NonNull;

import io.reactivex.CompletableTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.SingleTransformer;

/**
 * <p>Created on 16/03/2018.</p>
 *
 * @author Pierrejean
 */

interface Threader {

    @NonNull
    <T> ObservableTransformer<T, T> forObservable();

    @NonNull
    <T> SingleTransformer<T, T> forSingle();

    @NonNull
    CompletableTransformer forCompletable();
}
