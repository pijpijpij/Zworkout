package com.pij.zworkout.service.android;

import com.annimon.stream.Optional;
import com.pij.zworkout.service.api.StorageService;
import com.pij.zworkout.service.api.Workout;
import com.pij.zworkout.service.api.WorkoutFile;
import com.pij.zworkout.service.api.WorkoutSerializerService;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Completable;
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
    private final WorkoutSerializerService serialiser;
    private final Pattern fileMatcher = Pattern.compile("(.*)\\.zwo$", Pattern.CASE_INSENSITIVE);

    public FolderStorageService(File root, WorkoutSerializerService serialiser) {
        this.root = root;
        this.serialiser = serialiser;
    }

    @Override
    public Observable<List<WorkoutFile>> workouts() {
        return Observable.defer(() -> Observable.fromArray(root.listFiles()))
                .flatMapMaybe(this::convert)
                .toList()
                .toObservable();
    }

    @Override
    public Completable save(Workout data, WorkoutFile file) {
        return Single.zip(
                Single.just(data),
                Single.just(file.uri()).map(File::new),
                serialiser::write)
                .flatMapCompletable(serialisation -> serialisation);
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
