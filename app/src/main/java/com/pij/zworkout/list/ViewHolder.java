package com.pij.zworkout.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.annimon.stream.function.Consumer;
import com.pij.zworkout.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <p>Created on 01/03/2018.</p>
 *
 * @author Pierrejean
 */
class ViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.content)
    TextView content;

    ViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, itemView);
    }

    void bind(WorkoutDescriptor item, Consumer<WorkoutDescriptor> clickAction) {
        name.setText(item.id());
        content.setText(item.name());
        itemView.setOnClickListener(view -> clickAction.accept(item));
    }
}
