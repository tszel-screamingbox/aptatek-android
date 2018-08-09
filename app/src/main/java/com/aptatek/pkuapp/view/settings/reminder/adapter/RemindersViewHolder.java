package com.aptatek.pkuapp.view.settings.reminder.adapter;

import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aptatek.pkuapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

class RemindersViewHolder extends RecyclerView.ViewHolder {

    @Nullable
    private final ReminderAdapterCallback callback;

    RemindersViewHolder(final View itemView, @Nullable final ReminderAdapterCallback callback) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.callback = callback;
    }

    @BindView(R.id.textViewTime)
    TextView textViewTime;

    @BindView(R.id.containerLayoutReminder)
    ConstraintLayout constraintLayoutReminder;

    void bind(final RemindersAdapterItem item) {
        textViewTime.setText(item.getTime());

        itemView.setOnClickListener(v -> {
            if (callback != null && item.isActive()) {
                callback.modifyReminderTime(item);
            }
        });

        if (item.isActive()) {
            constraintLayoutReminder.setBackgroundResource(R.drawable.reminder_item_active_background);
        } else {
            constraintLayoutReminder.setBackgroundResource(R.drawable.reminder_item_inactive_background);
        }
    }
}
