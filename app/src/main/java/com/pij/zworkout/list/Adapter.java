package com.pij.zworkout.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.function.Consumer;
import com.pij.zworkout.R;

import java.util.List;

import static java.util.Collections.emptyList;

/**
 * <p>Created on 01/03/2018.</p>
 *
 * @author Pierrejean
 */
class Adapter extends RecyclerView.Adapter<ViewHolder> {

    private final Consumer<WorkoutInfo> clickAction;
    private List<WorkoutInfo> items;

    Adapter(Consumer<WorkoutInfo> clickAction) {
        this.items = emptyList();
        this.clickAction = clickAction;
    }

    void setItems(@NonNull List<WorkoutInfo> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        WorkoutInfo item = items.get(position);
        holder.bind(item, clickAction);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
