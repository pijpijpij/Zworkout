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
import android.widget.ArrayAdapter
import com.pij.TextWatcherAdapter
import com.pij.set
import com.pij.updateText
import com.pij.zworkout.R
import kotlinx.android.synthetic.main.efforts_item_steady_state.view.*
import java.util.Collections.emptyList

/**
 * @author Pierrejean
 */
internal class EffortsAdapter(private val change: (ModelEffort, Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var values: List<ModelEffort> = emptyList()

    private val steadyStateLayout: Int = R.layout.efforts_item_steady_state

    override fun getItemViewType(position: Int): Int {
        return when (values[position]) {
            is ModelSteadyState -> R.layout.efforts_item_steady_state
            else -> throw UnsupportedOperationException("Not coded yet")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        when (viewType) {
            steadyStateLayout -> return SteadyStateViewHolder(view, this::storeAndForward)
            else -> throw UnsupportedOperationException("Not coded yet")
        }
    }

    private fun storeAndForward(effort: ModelEffort, position: Int) {
        values = values.set(position, effort)
        change.invoke(effort, position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SteadyStateViewHolder -> holder.setItem(values[position] as ModelSteadyState)
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
        val diff = DiffUtil.calculateDiff(PlaceDiffCallback(oldValues, items))
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

private class SteadyStateViewHolder(view: View, private val clickAction: (ModelEffort, Int) -> Unit) : RecyclerView.ViewHolder(view) {

    private lateinit var item: ModelSteadyState

    init {
        with(itemView) {
            power.setAdapter(ArrayAdapter<String>(itemView.context,
                    // TODO get that data from the model
                    android.R.layout.simple_dropdown_item_1line, ModelPowerRange.values().map { it.name }))

            duration.addTextChangedListener(TextWatcherAdapter {
                item = item.copy(duration = it.toDuration())
                clickAction(item, adapterPosition)
            })
            power.addTextChangedListener(TextWatcherAdapter {
                item = item.copy(power = it.toPower())
                clickAction(item, adapterPosition)
            })
            cadence.addTextChangedListener(TextWatcherAdapter {
                item = item.copy(cadence = it.toCadence())
                clickAction(item, adapterPosition)
            })
        }
    }

    protected fun String.toDuration() = toInt()

    protected fun String.toCadence() = toInt()

    protected fun String.toPower(): ModelPower {
        return try {
            val range = ModelPowerRange.valueOf(this)
            ModelRangedPower(range)
        } catch (e: IllegalArgumentException) {
            try {
                val fraction = toFloat()
                ModelRelativePower(fraction)
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                ModelRangedPower(ModelPowerRange.Z1)
            }
        }
    }

    fun setItem(item: ModelSteadyState) {
        this.item = item
        itemView.apply {
            duration.updateText(item.duration.toString())
            power.updateText(item.power.toHumanReadable())
            cadence.updateText(item.cadence?.toString())
        }
    }

    private fun ModelPower.toHumanReadable(): String {
        return when (this) {
            is ModelRelativePower -> fraction.toString()
            is ModelRangedPower -> range.name
        }
    }

}

