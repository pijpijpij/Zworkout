package com.pij.zworkout.service.api;

import com.annimon.stream.Optional;

/**
 * <p>Created on 12/03/2018.</p>
 *
 * @author Pierrejean
 */
public class WorkoutFileTestUtil {

    public static WorkoutFile empty() {
        return WorkoutFile.create(Optional.empty(), "", Optional.empty());
    }

}