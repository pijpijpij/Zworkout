package com.pij.zworkout.persistence.api;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * <p>Created on 12/03/2018.</p>
 *
 * @author Pierrejean
 */
@Root(name = "workout_file")
public class PersistableWorkout {

    @Element
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
