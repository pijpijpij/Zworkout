package com.pij.zworkout.service.api;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.CheckReturnValue;

/**
 * <p>Created on 02/03/2018.</p>
 *
 * @author Pierrejean
 */

public interface StorageService {

    @CheckReturnValue
    Observable<List<WorkoutFile>> workouts();

    @CheckReturnValue
    Single<File> create(String name);

    @CheckReturnValue
    Single<OutputStream> openForWrite(File target);

    @CheckReturnValue
    Single<InputStream> openForRead(File source);
}
