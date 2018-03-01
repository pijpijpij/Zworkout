package com.pij.zworkout.list;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;

/**
 * <p>Created on 01/03/2018.</p>
 *
 * @author Pierrejean
 */

interface ViewModel {

    void load();

    @NotNull
    Observable<Model> model();
}
