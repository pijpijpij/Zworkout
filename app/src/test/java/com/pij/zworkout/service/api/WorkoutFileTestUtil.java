package com.pij.zworkout.service.api;

import com.annimon.stream.Optional;

import java.net.URI;

/**
 * <p>Created on 12/03/2018.</p>
 *
 * @author Pierrejean
 */
public class WorkoutFileTestUtil {

    public static WorkoutFile empty() {
        return WorkoutFile.create(URI.create("hello"), "", Optional.empty());
    }

}