package com.pij.zworkout.workout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.annimon.stream.Optional;
import com.pij.zworkout.R;
import com.pij.zworkout.list.WorkoutInfo;
import com.pij.zworkout.list.WorkoutsActivity;

import activitystarter.ActivityStarter;
import activitystarter.Arg;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import static com.annimon.stream.Optional.ofNullable;


/**
 * An activity representing a single Workout detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link WorkoutsActivity}.
 */
public class WorkoutDetailActivity extends DaggerAppCompatActivity {

    @Arg(optional = true)
    String itemId;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public static Intent createIntent(Context caller, Optional<WorkoutInfo> item) {
        return item.map(workout -> WorkoutDetailActivityStarter.getIntent(caller, workout.id()))
                .orElse(WorkoutDetailActivityStarter.getIntent(caller));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStarter.fill(this, savedInstanceState);
        setContentView(R.layout.activity_workout_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        // Show the Up button in the action bar.
        ofNullable(getSupportActionBar()).ifPresent(actionBar -> actionBar.setDisplayHomeAsUpEnabled(true));

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            WorkoutDetailFragment fragment = WorkoutDetailFragmentStarter.newInstance(itemId);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.workout_detail_container, fragment)
                    .commit();
        }

    }

    @OnClick(R.id.fab)
    void showASnackbar(View view) {
        Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(WorkoutsActivity.createIntent(this));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
