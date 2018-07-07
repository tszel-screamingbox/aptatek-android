package com.aptatek.aptatek.view.settings.reminder.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.util.Constants;
import com.aptatek.aptatek.view.base.BaseAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReminderSettingsAdapter extends BaseAdapter<ReminderSettingsAdapter.ReminderSettingsViewHolder, ReminderSettingsAdapterItem> {

    @Nullable
    private ReminderSettingsAdapterCallback callback;

    @Inject
    ReminderSettingsAdapter() {
    }

    public void setCallback(@Nullable final ReminderSettingsAdapterCallback callback) {
        this.callback = callback;
    }

    @Override
    protected ReminderSettingsViewHolder setViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return new ReminderSettingsViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.view_daily_reminder_item, parent, false));
    }

    @Override
    protected void bindData(@NonNull final ReminderSettingsViewHolder holder, final int position) {
        holder.bind(data.getCurrentList().get(holder.getAdapterPosition()));
    }

    public class ReminderSettingsViewHolder extends RecyclerView.ViewHolder implements ReminderAdapterCallback {

        @BindView(R.id.textViewDayName)
        TextView textViewDayName;

        @BindView(R.id.switchActivate)
        SwitchCompat switchActivate;

        @BindView(R.id.imageAddNewReminder)
        ImageView imageAddNewReminder;

        @BindView(R.id.recyclerViewReminders)
        RecyclerView recyclerViewReminders;

        private final RemindersAdapter adapter = new RemindersAdapter();

        ReminderSettingsViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            recyclerViewReminders.setAdapter(adapter);
            recyclerViewReminders.setLayoutManager(new GridLayoutManager(itemView.getContext(), Constants.REMINDER_SPAN_COUNT));
            recyclerViewReminders.addItemDecoration(new ReminderItemDecoration());
            adapter.setCallback(this);
        }

        void bind(final ReminderSettingsAdapterItem item) {
            adapter.setData(item.getReminders());

            textViewDayName.setText(item.getNameOfDay());
            switchActivate.setChecked(item.isActive());
            textViewDayName.setEnabled(item.isActive());

            if (item.isActive()) {
                imageAddNewReminder.setVisibility(View.VISIBLE);
            } else {
                imageAddNewReminder.setVisibility(View.GONE);
            }

            imageAddNewReminder.setOnClickListener(v -> {
                if (callback != null) {
                    callback.onAddReminderClicked(data.getCurrentList().get(getAdapterPosition()));
                }
            });

            switchActivate.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (callback != null && data.getCurrentList().get(getAdapterPosition()).isActive() != isChecked) {
                    callback.changeActiveState(data.getCurrentList().get(getAdapterPosition()), isChecked);
                }
            });
        }

        @Override
        public void modifyReminderTime(@NonNull final RemindersAdapterItem item) {
            if (callback != null) {
                callback.modifyReminderTime(data.getCurrentList().get(getAdapterPosition()), item);
            }
        }
    }
}
