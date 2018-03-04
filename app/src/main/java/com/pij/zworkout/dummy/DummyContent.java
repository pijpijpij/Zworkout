package com.pij.zworkout.dummy;

import android.support.annotation.NonNull;

import com.annimon.stream.Optional;
import com.pij.zworkout.list.WorkoutDescriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, WorkoutDescriptor> ITEM_MAP = new HashMap<>();
    /**
     * An array of sample (dummy) items.
     */
    private static final List<WorkoutDescriptor> ITEMS = new ArrayList<>();
    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(WorkoutDescriptor item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id(), item);
    }

    private static WorkoutDescriptor createDummyItem(int position) {
        return WorkoutDescriptor.builder()
                .id(Integer.toString(position))
                .name("Item " + position)
                .details(Optional.of(makeDetails(position)))
                .build();
    }

    @NonNull
    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

}
