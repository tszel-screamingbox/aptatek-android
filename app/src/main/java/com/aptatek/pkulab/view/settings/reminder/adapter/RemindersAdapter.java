package com.aptatek.pkulab.view.settings.reminder.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.view.base.BaseAdapter;

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
