package com.pij.zworkout.workout;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annimon.stream.Optional;
import com.pij.zworkout.R;
import com.pij.zworkout.dummy.DummyContent;
import com.pij.zworkout.list.WorkoutInfo;
import com.pij.zworkout.list.WorkoutsActivity;

import activitystarter.ActivityStarter;
import activitystarter.Arg;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;

import static com.annimon.stream.Optional.ofNullable;

/**
 * A fragment representing a single Workout detail screen.
 * This fragment is either contained in a {@link WorkoutsActivity}
 * in two-pane mode (on tablets) or a {@link WorkoutDetailActivity}
 * on handsets.
 */
public class WorkoutDetailFragment extends DaggerFragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    @Arg(optional = true)
    String itemId;

    @BindView(R.id.name)
    TextView details;

    /**
     * The dummy content this fragment is presenting.
     */
    private WorkoutInfo item;
    private Unbinder unbinder;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WorkoutDetailFragment() {
    }

    @NonNull
    public static WorkoutDetailFragment newInstance(Optional<String> itemId) {
        return itemId.map(WorkoutDetailFragmentStarter::newInstance)
                .orElse(WorkoutDetailFragmentStarter.newInstance());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.workout_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ActivityStarter.fill(this, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        if (itemId != null) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            item = DummyContent.ITEM_MAP.get(itemId);

            ofNullable(getActivity())
                    .map(activity -> activity.findViewById(R.id.toolbar_layout))
                    .map(toolbar -> (CollapsingToolbarLayout) toolbar)
                    .ifPresent(appBarLayout -> appBarLayout.setTitle(item.name()));
        }

        // Show the dummy content as text in a TextView.
        details.setText(item == null ? null : item.details().orElse("-"));

    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        unbinder = null;
        super.onDestroyView();
    }
}
