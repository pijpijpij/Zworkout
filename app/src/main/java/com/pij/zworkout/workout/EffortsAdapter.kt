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

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pij.zworkout.R
import kotlinx.android.synthetic.main.efforts_item_steady_state.view.*
import java.util.Collections.emptyList

/**
 * @author Pierrejean
 */
internal class EffortsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var values: List<Effort> = emptyList()

    private val steadyStateLayout: Int = R.layout.efforts_item_steady_state

    override fun getItemViewType(position: Int): Int {
        val item = values[position]
        when (item) {
            is SteadyState -> R.layout.efforts_item_steady_state
            else -> throw UnsupportedOperationException("Not coded yet")
        }
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        when (viewType) {
            steadyStateLayout -> return SteadyStateViewHolder(view)
            else -> throw UnsupportedOperationException("Not coded yet")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SteadyStateViewHolder -> holder.setItem(values[position] as SteadyState)
            else -> throw UnsupportedOperationException("Not coded yet")
        }
    }

    override fun getItemCount(): Int {
        return values.size
    }

    /**
     * Animates the insertions/ addition of items.
     */
    fun setItems(items: List<Effort>) {
        val oldValues = values
        values = items.toList()
        val diff = DiffUtil.calculateDiff(PlaceDiffCallback(oldValues, items))
        diff.dispatchUpdatesTo(this)
    }

    private class PlaceDiffCallback(private val oldValues: List<Effort>, private val newValues: List<Effort>) : DiffUtil.Callback() {

        override fun getOldListSize() = oldValues.size

        override fun getNewListSize() = newValues.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldValues[oldItemPosition]
            val newItem = newValues[newItemPosition]
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldValues[oldItemPosition]
            val newItem = newValues[newItemPosition]
            return oldItem == newItem
        }

    }
}

private class SteadyStateViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun setItem(item: SteadyState) {
        itemView.apply {
            duration.text = resources.getString(R.string.workout_efforts_item_steady_state_duration, item.duration)
        }
    }

}
