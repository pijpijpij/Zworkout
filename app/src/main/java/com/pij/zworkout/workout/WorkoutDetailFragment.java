package com.pij.zworkout.workout;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annimon.stream.Optional;
import com.pij.zworkout.R;
import com.pij.zworkout.list.WorkoutsActivity;

import javax.inject.Inject;

import activitystarter.ActivityStarter;
import activitystarter.Arg;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;
import io.reactivex.disposables.CompositeDisposable;

import static com.annimon.stream.Optional.ofNullable;

/**
 * A fragment representing a single Workout detail screen.
 * This fragment is either contained in a {@link WorkoutsActivity}
 * in two-pane mode (on tablets) or a {@link WorkoutDetailActivity}
 * on handsets.
 */
public class WorkoutDetailFragment extends DaggerFragment {
    private final CompositeDisposable subscriptions = new CompositeDisposable();
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    @Arg(optional = true)
    String itemId;
    @BindView(R.id.workout_detail)
    View workoutDetail;
    @BindView(R.id.name)
    TextView name;
    @Inject
    ViewModel viewModel;
    private Unbinder unbinder;

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
        subscriptions.addAll(
                viewModel.model().map(Model::inProgress).subscribe(this::showInProgress),
                viewModel.model().map(Model::showError).filter(Optional::isPresent).map(Optional::get).subscribe(this::showError),
                viewModel.model().map(Model::name).subscribe(this::setName)
        );

        if (savedInstanceState == null) {
            if (itemId == null) {
                viewModel.createWorkout();
            } else {
                viewModel.load(itemId);
            }
        }
    }

    private void setName(String newValue) {
        name.setText(newValue);
        // TODO put that in a different place in the lifecycle
        ofNullable(getActivity())
                .map(activity -> activity.findViewById(R.id.toolbar_layout))
                .map(toolbar -> (CollapsingToolbarLayout) toolbar)
                .ifPresent(appBarLayout -> appBarLayout.setTitle(newValue));
    }

    @Override
    public void onDestroyView() {
        subscriptions.clear();
        unbinder.unbind();
        unbinder = null;
        super.onDestroyView();
    }

    private void showInProgress(boolean inProgress) {
//        empty.setText(inProgress ? R.string.list_loading : R.string.list_empty);
    }

    private void showError(String message) {
        Snackbar.make(workoutDetail, message, Snackbar.LENGTH_LONG).show();
    }

}
