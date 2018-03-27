package com.pij.zworkout.workout

import activitystarter.ActivityStarter
import activitystarter.Arg
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.util.ObjectsCompat
import android.view.*
import com.pij.utils.TextWatcherAdapter
import com.pij.zworkout.R
import com.pij.zworkout.list.WorkoutsActivity
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_workout_detail.*
import kotlinx.android.synthetic.main.workout_detail.*
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
    //    @BindView(R.id.workout_detail)
//    internal var workoutDetail: View? = null
//    @BindView(R.id.name)
//    internal var name: TextView? = null
//    @BindView(R.id.description)
//    internal var description: TextView? = null
    @Inject
    internal lateinit var viewModel: WorkoutViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.workout_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ActivityStarter.fill(this, savedInstanceState)
        setHasOptionsMenu(true)

        name.addTextChangedListener(TextWatcherAdapter { viewModel.name(it) })
        description.addTextChangedListener(TextWatcherAdapter { viewModel.description(it) })

        subscriptions.addAll(
                viewModel.model().filter { it.showError != null }.subscribe { showError(it.showError!!) },
                viewModel.model().map { it.inProgress }.distinctUntilChanged().subscribe { showInProgress(it) },
                viewModel.model().map { it.name }.distinctUntilChanged().subscribe { displayName(it) },
                viewModel.model().map { it.nameIsReadOnly }.distinctUntilChanged().subscribe { disableName(it) },
                viewModel.model().map { it.description }.distinctUntilChanged().subscribe { displayDescription(it) }
        )

        if (savedInstanceState == null) {
            if (itemId == null) {
                viewModel.createWorkout()
            } else {
                viewModel.load(itemId!!)
            }
        }
    }

    override fun onDestroyView() {
        subscriptions.clear()
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.workout_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // Handle item selection
        return when (item!!.itemId) {
            R.id.menu_save -> {
                viewModel.save()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun displayDescription(newValue: String) {
        if (!ObjectsCompat.equals(description.text.toString(), newValue)) {
            description.setText(newValue)
        }
    }

    private fun disableName(readOnly: Boolean) {
        name!!.isEnabled = !readOnly
    }

    private fun displayName(newValue: String) {
        if (!ObjectsCompat.equals(name.text.toString(), newValue)) {
            name.setText(newValue)
        }
        // TODO put that in a different place in the lifecycle
        toolbar_layout?.title = newValue
    }

    private fun showInProgress(inProgress: Boolean) {
//        empty.setText(if (inProgress) R.string.list_loading else R.string.list_empty)
    }

    private fun showError(message: String) {
        Snackbar.make(workout_detail, message, Snackbar.LENGTH_LONG).show()
    }

    companion object {

        fun newInstance(itemId: String?): WorkoutDetailFragment {
            return itemId?.let { WorkoutDetailFragmentStarter.newInstance(it) } ?: WorkoutDetailFragmentStarter.newInstance()
        }
    }

}

