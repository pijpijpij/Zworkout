package com.pij.zworkout.service.android;

import com.annimon.stream.Optional;
import com.pij.zworkout.service.api.StorageService;
import com.pij.zworkout.service.api.WorkoutFile;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Maybe;
import io.reactivex.Observable;

/**
 * <p>Created on 02/03/2018.</p>
 *
 * @author Pierrejean
 */

public class FolderStorageService implements StorageService {

    private final File root;
    private final Pattern fileMatcher = Pattern.compile("(.*)\\.zwo$", Pattern.CASE_INSENSITIVE);

    public FolderStorageService(File root) {
        this.root = root;
    }

    @Override
    public Observable<List<WorkoutFile>> workouts() {
        return Observable.defer(() -> Observable.fromArray(root.listFiles()))
                .flatMapMaybe(this::convert)
                .toList()
                .toObservable();
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
