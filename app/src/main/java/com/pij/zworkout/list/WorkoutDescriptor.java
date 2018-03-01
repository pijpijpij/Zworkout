package com.pij.zworkout.list;

/**
 * A dummy item representing a piece of content.
 */
public class WorkoutDescriptor {
    public final String id;
    public final String content;
    public final String details;

    @SuppressWarnings("WeakerAccess")
    public WorkoutDescriptor(String id, String content, String details) {
        this.id = id;
        this.content = content;
        this.details = details;
    }

    @Override
    public String toString() {
        return content;
    }
}
