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

package com.pij.zworkout.workout

import activitystarter.ActivityStarter
import activitystarter.Arg
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.*
import androidx.core.view.isVisible
import androidx.core.widget.toast
import com.pij.TextWatcherAdapter
import com.pij.updateEnabled
import com.pij.updateText
import com.pij.zworkout.R
import com.pij.zworkout.list.WorkoutsActivity
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_workout_detail.*
import kotlinx.android.synthetic.main.fragment_workout.*
import javax.inject.Inject

/**
 * A fragment representing a single Workout detail screen.
 * This fragment is either contained in a [WorkoutsActivity]
 * in two-pane mode (on tablets) or a [WorkoutDetailActivity]
 * on handsets.
 */
class WorkoutDetailFragment : DaggerFragment() {
    private val subscriptions = CompositeDisposable()
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    @Arg(optional = true)
    var itemId: String? = null

    @Inject
    internal lateinit var viewModel: WorkoutViewModel

    @Inject
    internal lateinit var adapter: EffortsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_workout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ActivityStarter.fill(this, savedInstanceState)
        setHasOptionsMenu(true)

        name.addTextChangedListener(TextWatcherAdapter { viewModel.name(it) })
        description.addTextChangedListener(TextWatcherAdapter { viewModel.description(it) })
        list.adapter = adapter

        subscriptions.add(viewModel.model().subscribe({ display(it) }, { it.printStackTrace(); activity?.toast("bad call!")?.show() }))

        if (savedInstanceState == null) {
            if (itemId == null) {
                viewModel.createWorkout()
            } else {
                viewModel.load(itemId!!)
            }
        }
    }

    private fun display(model: Model) {
        if (model.showError != null) {
            Snackbar.make(workout_detail, model.showError, Snackbar.LENGTH_LONG).show()
        }

        empty.setText(if (model.inProgress) R.string.detail_loading else R.string.detail_empty)
        name.updateText(model.name)
        toolbar_layout?.title = model.name
        name.updateEnabled(!model.nameIsReadOnly)
        description.updateText(model.description)
        list.isVisible = model.efforts.isNotEmpty()
        empty.isVisible = model.efforts.isEmpty()
        adapter.setItems(model.efforts)
    }

    override fun onDestroyView() {
        subscriptions.clear()
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.workout_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.menu_save -> {
                viewModel.save()
                true
            }
            R.id.menu_add -> {
                viewModel.addEffort()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {

        fun newInstance(itemId: String?): WorkoutDetailFragment {
            return itemId?.let { WorkoutDetailFragmentStarter.newInstance(it) } ?: WorkoutDetailFragmentStarter.newInstance()
        }
    }

}

