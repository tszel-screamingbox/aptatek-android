package com.aptatek.aptatek.view.settings.reminder;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aptatek.aptatek.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RemindersAdapter extends RecyclerView.Adapter<RemindersAdapter.RemindersViewHolder> {

    private ArrayList<RemindersAdapterItem> data = new ArrayList<>();

    @Nullable
    private ReminderAdapterCallback callback;

    public void setCallback(@Nullable final ReminderAdapterCallback callback) {
        this.callback = callback;
    }

    public void setData(final ArrayList<RemindersAdapterItem> mData) {
        data.clear();
        data.addAll(mData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RemindersViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return new RemindersViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_reminder_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RemindersViewHolder holder,final int position) {
        holder.bind(data.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class RemindersViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewTime)
        TextView textViewTime;

        @BindView(R.id.containerLayoutReminder)
        ConstraintLayout constraintLayoutReminder;

        RemindersViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(final RemindersAdapterItem item) {
            textViewTime.setText(item.getTime());

            itemView.setOnLongClickListener(v -> {
                if (callback != null && data.get(getAdapterPosition()).getActive()) {
                    callback.modifyReminderTime(data.get(getAdapterPosition()));
                }
                return true;
            });

            if (item.getActive()) {
                constraintLayoutReminder.setBackgroundResource(R.drawable.reminder_item_active_background);
            } else {
                constraintLayoutReminder.setBackgroundResource(R.drawable.reminder_item_inactive_background);
            }
        }
    }

    interface ReminderAdapterCallback {
        void modifyReminderTime(@NonNull RemindersAdapterItem item);
    }
}
