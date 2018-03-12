package com.pij.zworkout.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.annimon.stream.Optional;
import com.annimon.stream.function.Consumer;
import com.pij.horrocks.Logger;
import com.pij.zworkout.R;
import com.pij.zworkout.workout.WorkoutDetailActivity;
import com.pij.zworkout.workout.WorkoutDetailFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.disposables.CompositeDisposable;

/**
 * An activity representing a list of Workouts. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link WorkoutDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class WorkoutsActivity extends DaggerAppCompatActivity {

    private final CompositeDisposable subscriptions = new CompositeDisposable();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(android.R.id.empty)
    TextView empty;
    @BindView(android.R.id.list)
    RecyclerView list;

    @Inject
    WorkoutsViewModel viewModel;
    @Inject
    Logger logger;
    @Inject
    Adapter adapter;

    public static Intent createIntent(Context caller) {
        return new Intent(caller, WorkoutsActivity.class);
    }

    private void showInFragment(Optional<WorkoutInfo> item) {
        WorkoutDetailFragment fragment = WorkoutDetailFragment.newInstance(item.map(WorkoutInfo::id));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.workout_detail_container, fragment)
                .commit();
    }


    private void showInActivity(Optional<WorkoutInfo> item) {
        Intent intent = WorkoutDetailActivity.createIntent(this, item);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_list);
        ButterKnife.bind(this);

        toolbar.setTitle(getTitle());
        setSupportActionBar(toolbar);
        list.setAdapter(adapter);

        subscriptions.addAll(
                viewModel.model().map(Model::inProgress).subscribe(this::showInProgress),
                viewModel.model().map(Model::showError).filter(Optional::isPresent).map(Optional::get).subscribe(this::showError),
                viewModel.model().map(Model::workouts).subscribe(this::showItems),
                viewModel.model().map(Model::showWorkout).filter(Optional::isPresent).subscribe(this::showWorkout),
                viewModel.model().map(Model::createWorkout).filter(create -> create).subscribe(trigger -> showWorkout(Optional.empty()))
        );
    }

    private void showWorkout(Optional<WorkoutInfo> workout) {
        // The detail container view will be present only in the
        // large-screen layouts (res/values-w900dp).
        // If this view is present, then the
        // activity should be in two-pane mode.
        boolean twoPane = findViewById(R.id.workout_detail_container) != null;
        Consumer<Optional<WorkoutInfo>> clickAction = twoPane ? this::showInFragment : this::showInActivity;

        clickAction.accept(workout);
    }

    private void showError(String message) {
        Snackbar.make(list, message, Snackbar.LENGTH_LONG).show();
    }

    private void showItems(List<WorkoutInfo> items) {
        adapter.setItems(items);
        list.setVisibility(items.isEmpty() ? View.GONE : View.VISIBLE);
        empty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void showInProgress(boolean inProgress) {
        empty.setText(inProgress ? R.string.list_loading : R.string.list_empty);
    }

    @Override
    protected void onDestroy() {
        subscriptions.clear();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.load();
    }

    @OnClick(R.id.fab)
    void createWorkout() {
        viewModel.createWorkout();
//        Snackbar.make(list, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null)
//                .show();
    }

}
