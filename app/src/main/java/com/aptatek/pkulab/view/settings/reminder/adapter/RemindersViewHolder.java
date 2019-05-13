package com.aptatek.pkulab.view.settings.reminder.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.aptatek.pkulab.R;

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

        textViewTime.setEnabled(item.isActive());
    }
}
