package com.pij.zworkout.persistence.api;

import android.support.annotation.Nullable;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

/**
 * <p>Created on 12/03/2018.</p>
 *
 * @author Pierrejean
 */
@Root(name = "workout_file")
@Default(DefaultType.FIELD)
public class PersistableWorkout {

    public EmptyString name;

    public static class EmptyString {
        @Text(required = false)
        public String value;

        public static EmptyString create(@Nullable String value) {
            EmptyString result = new EmptyString();
            result.value = value;
            return result;
        }
    }

}
