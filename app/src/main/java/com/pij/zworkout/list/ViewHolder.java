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

    void bind(WorkoutInfo item, Consumer<WorkoutInfo> clickAction) {
        name.setText(item.name());
        item.details().ifPresent(content::setText);
        itemView.setOnClickListener(view -> clickAction.accept(item));
    }
}
