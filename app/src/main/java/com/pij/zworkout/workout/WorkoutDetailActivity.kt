package com.pij.zworkout.workout

import activitystarter.ActivityStarter
import activitystarter.Arg
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.MenuItem
import android.view.View
import com.pij.zworkout.R
import com.pij.zworkout.list.WorkoutInfo
import com.pij.zworkout.list.WorkoutsActivity
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_workout_detail.*


/**
 * An activity representing a single Workout detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [WorkoutsActivity].
 */
class WorkoutDetailActivity : DaggerAppCompatActivity() {

    @Arg(optional = true)
    var itemId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityStarter.fill(this, savedInstanceState)
        setContentView(R.layout.activity_workout_detail)

        setSupportActionBar(toolbar)
        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fab.setOnClickListener { showASnackbar(it) }

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            val fragment = WorkoutDetailFragment.newInstance(itemId)
            supportFragmentManager.beginTransaction()
                    .add(R.id.workout_detail_container, fragment)
                    .commit()
        }

    }

    private fun showASnackbar(view: View) {
        Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(WorkoutsActivity.createIntent(this))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        fun createIntent(caller: Context, item: WorkoutInfo?): Intent {
            return item?.let { WorkoutDetailActivityStarter.getIntent(caller, it.id) } ?: WorkoutDetailActivityStarter.getIntent(caller)
        }
    }
}
