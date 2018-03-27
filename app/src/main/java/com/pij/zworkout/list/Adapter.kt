package com.pij.zworkout.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pij.zworkout.R
import java.util.Collections.emptyList

/**
 *
 * Created on 01/03/2018.
 *
 * @author Pierrejean
 */
internal class Adapter(private val clickAction: (WorkoutInfo) -> Unit) : RecyclerView.Adapter<ViewHolder>() {

    /** Write-only property.
     *
     */
    private var items: List<WorkoutInfo> = emptyList()

    fun setItems(items: List<WorkoutInfo>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.workout_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, clickAction)
    }

    override fun getItemCount(): Int {
        return items.size
    }

}
