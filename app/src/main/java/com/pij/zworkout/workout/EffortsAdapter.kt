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
import com.pij.updateError
import com.pij.updateText
import com.pij.zworkout.R
import kotlinx.android.synthetic.main.efforts_item_steady_state.view.*

/**
 * @author Pierrejean
 */
internal class EffortsAdapter(
        private val applyChange: (ModelEffort, Int) -> Unit,
        private val propertyEditor: (EffortProperty) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var values = listOf<ModelEffort>()

    private val steadyStateLayout = R.layout.efforts_item_steady_state

    override fun getItemViewType(position: Int): Int {
        return when (values[position]) {
            is ModelSteadyState -> R.layout.efforts_item_steady_state
            else -> throw UnsupportedOperationException("Not coded yet")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        when (viewType) {
            steadyStateLayout -> return SteadyStateViewHolder(view, propertyEditor)
            else -> throw UnsupportedOperationException("Not coded yet")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SteadyStateViewHolder -> {
                holder.display(values[position] as ModelSteadyState)
            }
            else -> throw UnsupportedOperationException("Not coded yet")
        }
    }

    override fun getItemCount(): Int {
        return values.size
    }

    /**
     * Animates the insertions/ addition of items.
     */
    fun setItems(items: List<ModelEffort>) {
        val oldValues = values
        values = items.toList()
        val diff = DiffUtil.calculateDiff(PlaceDiffCallback(oldValues, values))
        diff.dispatchUpdatesTo(this)
    }

    private class PlaceDiffCallback(private val oldValues: List<ModelEffort>, private val newValues: List<ModelEffort>) : DiffUtil.Callback() {

        override fun getOldListSize() = oldValues.size

        override fun getNewListSize() = newValues.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItemPosition == newItemPosition
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldValues[oldItemPosition]
            val newItem = newValues[newItemPosition]
            return oldItem == newItem
        }

    }
}

private class SteadyStateViewHolder(
        view: View,
        private val editor: (EffortProperty) -> Unit
) : RecyclerView.ViewHolder(view) {

    init {
        println("PJC creating ViewHolder ${hashCode()} for view ${itemView.hashCode()}")
        with(itemView) {
            power.setOnClickListener { editor(SteadyStatePowerProperty(adapterPosition, power.text.toString())) }
        }
    }


    fun display(item: ModelSteadyState) {
        println("PJC ${hashCode()}.setItem($item) for view ${itemView.hashCode()}")
        try {
            itemView.apply {
                duration.updateText(item.duration.toString())
                power.updateError(item.powerError)
                power.updateText(item.power)
                cadence.updateText(item.cadence?.toString())
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            throw e
        }
    }

}
