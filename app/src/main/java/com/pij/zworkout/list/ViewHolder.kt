package com.pij.zworkout.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.pij.zworkout.R

/**
 *
 * Created on 01/03/2018.
 *
 * @author Pierrejean
 */
internal class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    @BindView(R.id.name)
    lateinit var name: TextView
    @BindView(R.id.content)
    lateinit var content: TextView

    init {
        ButterKnife.bind(this, itemView)
    }

    fun bind(item: WorkoutInfo, clickAction: (WorkoutInfo) -> Unit) {
        name.text = item.name
        content.text = item.details
        itemView.setOnClickListener { _ -> clickAction.invoke(item) }
    }
}
