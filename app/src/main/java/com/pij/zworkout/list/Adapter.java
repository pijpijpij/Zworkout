package com.pij.zworkout.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.function.Consumer;
import com.pij.zworkout.R;

import java.util.List;

/**
 * <p>Created on 01/03/2018.</p>
 *
 * @author Pierrejean
 */
class Adapter extends RecyclerView.Adapter<ViewHolder> {

    private final List<WorkoutDescriptor> items;
    private final Consumer<WorkoutDescriptor> clickAction;

    Adapter(List<WorkoutDescriptor> items, Consumer<WorkoutDescriptor> clickAction) {
        this.items = items;
        this.clickAction = clickAction;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        WorkoutDescriptor item = items.get(position);
        holder.bind(item, clickAction);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
