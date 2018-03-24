package com.pij.zworkout.workout;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.util.ObjectsCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import butterknife.OnTextChanged;
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
    @BindView(R.id.description)
    TextView description;
    @Inject
    WorkoutViewModel viewModel;
    private Unbinder unbinder;

    public WorkoutDetailFragment() {
    }

    @NonNull
    public static WorkoutDetailFragment newInstance(Optional<String> itemId) {
        return itemId.map(WorkoutDetailFragmentStarter::newInstance)
                .orElse(WorkoutDetailFragmentStarter.newInstance());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.workout_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ActivityStarter.fill(this, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        subscriptions.addAll(
                viewModel.model().map(Model::showError).filter(Optional::isPresent).map(Optional::get).subscribe(this::showError),
                viewModel.model().map(Model::inProgress).distinctUntilChanged().subscribe(this::showInProgress),
                viewModel.model().map(Model::name).distinctUntilChanged().subscribe(this::displayName),
                viewModel.model().map(Model::nameIsReadOnly).distinctUntilChanged().subscribe(this::disableName),
                viewModel.model().map(Model::description).distinctUntilChanged().subscribe(this::displayDescription)
        );

        if (savedInstanceState == null) {
            if (itemId == null) {
                viewModel.createWorkout();
            } else {
                viewModel.load(itemId);
            }
        }
    }

    @Override
    public void onDestroyView() {
        subscriptions.clear();
        unbinder.unbind();
        unbinder = null;
        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.workout_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_save:
                viewModel.save();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnTextChanged(R.id.name)
    void setName(CharSequence newValue) {
        viewModel.name(newValue.toString());
    }

    @OnTextChanged(R.id.description)
    void setDescription(CharSequence newValue) {
        viewModel.description(newValue.toString());
    }

    private void displayDescription(String newValue) {
        if (!ObjectsCompat.equals(description.getText().toString(), newValue)) {
            description.setText(newValue);
        }
    }

    private void disableName(boolean readOnly) {
        name.setEnabled(!readOnly);
    }

    private void displayName(String newValue) {
        if (!ObjectsCompat.equals(name.getText().toString(), newValue)) {
            name.setText(newValue);
        }
        // TODO put that in a different place in the lifecycle
        ofNullable(getActivity())
                .map(activity -> activity.findViewById(R.id.toolbar_layout))
                .map(toolbar -> (CollapsingToolbarLayout) toolbar)
                .ifPresent(appBarLayout -> appBarLayout.setTitle(newValue));
    }

    private void showInProgress(boolean inProgress) {
//        empty.setText(inProgress ? R.string.list_loading : R.string.list_empty);
    }

    private void showError(String message) {
        Snackbar.make(workoutDetail, message, Snackbar.LENGTH_LONG).show();
    }

}
