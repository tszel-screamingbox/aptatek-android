package com.aptatek.aptatek.view.settings.reminder.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.view.base.BaseAdapter;

public class RemindersAdapter extends BaseAdapter<RemindersViewHolder, RemindersAdapterItem> {

    @Nullable
    private ReminderAdapterCallback callback;

    public void setCallback(@Nullable final ReminderAdapterCallback callback) {
        this.callback = callback;
    }

    @Override
    protected RemindersViewHolder setViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return new RemindersViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.view_reminder_item, parent, false),
                callback);
    }

    @Override
    protected void bindData(@NonNull final RemindersViewHolder holder, final int position) {
        holder.bind(data.getCurrentList().get(holder.getAdapterPosition()));
    }
}
