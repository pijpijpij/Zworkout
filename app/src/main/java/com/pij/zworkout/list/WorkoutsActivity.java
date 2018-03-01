package com.pij.zworkout.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.annimon.stream.function.Consumer;
import com.pij.horrocks.Logger;
import com.pij.zworkout.R;
import com.pij.zworkout.workout.WorkoutDetailActivity;
import com.pij.zworkout.workout.WorkoutDetailFragment;

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
    @BindView(R.id.workout_list)
    RecyclerView recyclerView;

    @Inject
    ViewModel viewModel;
    @Inject
    Logger logger;

    public static Intent createIntent(Context caller) {
        return new Intent(caller, WorkoutsActivity.class);
    }

    private void showInFragment(WorkoutDescriptor item) {
        WorkoutDetailFragment fragment = WorkoutDetailFragment.newInstance(item);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.workout_detail_container, fragment)
                .commit();
    }


    private void showInActivity(WorkoutDescriptor item) {
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

        // The detail container view will be present only in the
        // large-screen layouts (res/values-w900dp).
        // If this view is present, then the
        // activity should be in two-pane mode.
        boolean twoPane = findViewById(R.id.workout_detail_container) != null;
        Consumer<WorkoutDescriptor> clickAction = twoPane ? this::showInFragment : this::showInActivity;
        Adapter adapter = new Adapter(clickAction);
        recyclerView.setAdapter(adapter);

        subscriptions.addAll(
                viewModel.model().map(Model::workouts).subscribe(adapter::setItems),
                viewModel.model().map(Model::inProgress).subscribe(value -> logger.print(getClass(), " not implemented yet"))
        );
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
    void showASnackbar(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show();
    }

}
