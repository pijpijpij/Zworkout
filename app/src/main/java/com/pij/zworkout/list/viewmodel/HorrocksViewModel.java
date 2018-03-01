package com.pij.zworkout.list.viewmodel;

import com.pij.horrocks.Configuration;
import com.pij.horrocks.Engine;
import com.pij.horrocks.Feature;
import com.pij.horrocks.Logger;
import com.pij.horrocks.MemoryStore;
import com.pij.horrocks.MultipleResultFeature;
import com.pij.horrocks.Result;
import com.pij.zworkout.list.Model;
import com.pij.zworkout.list.ViewModel;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

/**
 * <p>Created on 01/03/2018.</p>
 *
 * @author Pierrejean
 */

public class HorrocksViewModel implements ViewModel {

    private final Logger logger;
    private final Observable<Model> modelStream;
    private final Feature<Object, Model> loader;

    private HorrocksViewModel(Logger logger, Engine<Model, Model> engine, Feature<Object, Model> loadingFeature) {
        this.logger = logger;

        loader = loadingFeature;
        Configuration<Model, Model> engineConfiguration = Configuration.<Model, Model>builder()
                .store(new MemoryStore<>(initialState()))
                .transientResetter(it -> it)
                .stateToModel(it -> it)
                .features(singletonList(loader))
                .build();
        modelStream = engine.runWith(engineConfiguration).share();
    }

    /**
     * Helper constructor.
     */
    public static HorrocksViewModel create(Logger logger,
                                           Engine<Model, Model> engine,
                                           Function<Object, Observable<Result<Model>>> loadingFeature) {
        return new HorrocksViewModel(logger, engine,
                new MultipleResultFeature<>(loadingFeature, logger)
        );
    }

    private Model initialState() {
        return Model.create(false, emptyList());
    }

    @Override
    public void load() {
        loader.trigger(new Object());
    }

    @NotNull
    @Override
    public Observable<Model> model() {
        return modelStream
                .doOnError(e -> logger.print(getClass(), "Terminal Damage!!!", e))
                .doOnComplete(() -> logger.print(getClass(), "takeView completed!!!"))
                ;
    }
}
