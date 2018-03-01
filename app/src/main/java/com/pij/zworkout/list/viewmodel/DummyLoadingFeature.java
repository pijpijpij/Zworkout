package com.pij.zworkout.list.viewmodel;

import com.pij.horrocks.Result;
import com.pij.zworkout.dummy.DummyContent;
import com.pij.zworkout.list.Model;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * <p>Created on 01/03/2018.</p>
 *
 * @author Pierrejean
 */

public class DummyLoadingFeature implements Function<Object, Observable<Result<Model>>> {
    @Override
    public Observable<Result<Model>> apply(Object trigger) throws Exception {
        return Observable.just(model -> model.toBuilder()
                .workouts(DummyContent.ITEMS)
                .build());
    }
}
