package com.pij.zworkout.list

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.workouts_item.view.*

/**
 *
 * Created on 01/03/2018.
 *
 * @author Pierrejean
 */
internal class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(item: WorkoutInfo, clickAction: (WorkoutInfo) -> Unit) {
        itemView.name.text = item.name
        itemView.content.text = item.details
        itemView.setOnClickListener { _ -> clickAction(item) }
    }
}
