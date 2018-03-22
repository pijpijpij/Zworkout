package com.pij.zworkout.service.android;

import com.annimon.stream.Optional;
import com.pij.utils.Logger;
import com.pij.zworkout.service.api.StorageService;
import com.pij.zworkout.service.api.WorkoutFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * <p>Created on 02/03/2018.</p>
 *
 * @author Pierrejean
 */

public class FolderStorageService implements StorageService {

    private final File root;
    private final Pattern fileMatcher = Pattern.compile("(.*)\\.zwo$", Pattern.CASE_INSENSITIVE);
    private final Logger logger;

    public FolderStorageService(File root, Logger logger) {
        this.root = root;
        this.logger = logger;
    }

    @Override
    public Observable<List<WorkoutFile>> workouts() {
        return Observable.defer(() -> Observable.fromArray(root.listFiles()))
                .flatMapMaybe(this::convert)
                .toList()
                .toObservable();
    }

    @Override
    public Single<OutputStream> open(WorkoutFile file) {
        return Single.just(file.uri()).map(File::new).map(FileOutputStream::new)
                .cast(OutputStream.class)
                .doOnError(e -> logger.print(getClass(), e, ""))
                ;
    }

    private Maybe<WorkoutFile> convert(File file) {
        Maybe<String> name = Maybe.just(file.getName())
                .map(fileMatcher::matcher)
                .onErrorComplete()
                .filter(Matcher::matches)
                .map(matcher -> matcher.group(1));

        return Maybe.zip(name, Maybe.just(file), this::createWorkoutFile);
    }

    private WorkoutFile createWorkoutFile(String workoutName, File file) {
        return WorkoutFile.create(file.toURI(), workoutName, Optional.empty());
    }
}
