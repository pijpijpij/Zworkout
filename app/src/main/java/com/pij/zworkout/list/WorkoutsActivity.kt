/*
 * Copyright (c) 2018, Chiswick Forest
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

package com.pij.zworkout.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import com.pij.utils.Logger
import com.pij.zworkout.R
import com.pij.zworkout.workout.WorkoutDetailActivity
import com.pij.zworkout.workout.WorkoutDetailFragment
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_workouts.*
import kotlinx.android.synthetic.main.workouts.*
import javax.inject.Inject

/**
 * An activity representing a list of Workouts. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [WorkoutDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class WorkoutsActivity : DaggerAppCompatActivity() {

    private val subscriptions = CompositeDisposable()

    @Inject
    internal lateinit var viewModel: WorkoutsViewModel
    @Inject
    internal lateinit var logger: Logger
    @Inject
    internal lateinit var adapter: Adapter

    private fun showInFragment(item: WorkoutInfo?) {
        val fragment = WorkoutDetailFragment.newInstance(item?.id)
        supportFragmentManager.beginTransaction()
                .replace(R.id.workout_detail_container, fragment)
                .commit()
    }


    private fun showInActivity(item: WorkoutInfo?) {
        startActivity(WorkoutDetailActivity.createIntent(this, item))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workouts)

        toolbar.title = title
        setSupportActionBar(toolbar)
        list.adapter = adapter

        fab.setOnClickListener { viewModel.createWorkout() }

        subscriptions.addAll(
                viewModel.model().map { it.inProgress }.distinctUntilChanged().subscribe { showInProgress(it) },
                viewModel.model().filter { it.showError != null }.subscribe { showError(it.showError!!) },
                viewModel.model().map { it.workouts }.subscribe { showItems(it) },
                viewModel.model().filter { it.showWorkout != null }.subscribe { showWorkout(it.showWorkout!!) },
                viewModel.model().filter { it.createWorkout }.subscribe { _ -> showWorkout(null) }
        )

        if (savedInstanceState == null) {
            viewModel.load()
        }
    }

    private fun showWorkout(workout: WorkoutInfo?) {
        // The detail container view will be present only in the
        // large-screen layouts (res/values-w900dp).
        // If this view is present, then the
        // activity should be in two-pane mode.
        val twoPane = findViewById<View>(R.id.workout_detail_container) != null
        if (twoPane) {
            this.showInFragment(workout)
        } else {
            this.showInActivity(workout)
        }
    }

    private fun showError(message: String) {
        Snackbar.make(list, message, Snackbar.LENGTH_LONG).show()
    }

    private fun showItems(items: List<WorkoutInfo>) {
        adapter.setItems(items)
        list.visibility = if (items.isEmpty()) View.GONE else View.VISIBLE
        empty.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun showInProgress(inProgress: Boolean) {
        empty.setText(if (inProgress) R.string.list_loading else R.string.list_empty)
    }

    override fun onDestroy() {
        subscriptions.clear()
        super.onDestroy()
    }

    companion object {

        fun createIntent(caller: Context): Intent {
            return Intent(caller, WorkoutsActivity::class.java)
        }
    }

}
