package com.aptatek.aptatek.view.settings.reminder;

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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

// TODO base class with DiffUtils
class ReminderSettingsAdapter extends RecyclerView.Adapter<ReminderSettingsAdapter.ReminderSettingsViewHolder> {

    private final List<ReminderSettingsAdapterItem> data = new ArrayList<>();

    @Nullable
    private ReminderSettingsAdapterCallback callback;

    @Inject
    ReminderSettingsAdapter() {
    }

    public void setData(@NonNull final List<ReminderSettingsAdapterItem> mData) {
        data.clear();
        data.addAll(mData);
        notifyDataSetChanged();
    }

    public void changeAdapterItem(@NonNull final ReminderSettingsAdapterItem item) {
        final int index = data.indexOf(item);
        notifyItemChanged(index);
    }

    public void setCallback(@Nullable final ReminderSettingsAdapterCallback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public ReminderSettingsViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,final int viewType) {
        return new ReminderSettingsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_daily_reminder_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ReminderSettingsViewHolder holder,final int position) {
        holder.bind(data.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    // TODO static/dedicated class -> adapter package
    class ReminderSettingsViewHolder extends RecyclerView.ViewHolder implements RemindersAdapter.ReminderAdapterCallback {

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
            textViewDayName.setText(item.getNameOfDay());
            switchActivate.setChecked(item.isActive());
            textViewDayName.setEnabled(item.isActive());

            if (item.isActive()) {
                imageAddNewReminder.setVisibility(View.VISIBLE);
            } else {
                imageAddNewReminder.setVisibility(View.GONE);
            }

            ((RemindersAdapter) recyclerViewReminders.getAdapter()).setData(item.getReminders());

            imageAddNewReminder.setOnClickListener(v -> {
                if (callback != null) {
                    callback.onAddReminderClicked(data.get(getAdapterPosition()));
                }
            });

            switchActivate.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (callback != null && data.get(getAdapterPosition()).isActive() != isChecked) {
                    callback.changeActiveState(data.get(getAdapterPosition()), isChecked);
                }
            });
        }

        @Override
        public void modifyReminderTime(@NonNull final RemindersAdapterItem item) {
            if (callback != null) {
                callback.modifyReminderTime(data.get(getAdapterPosition()), item);
            }

        }
    }

    // TODO move outside
    interface ReminderSettingsAdapterCallback {
        void onAddReminderClicked(@NonNull ReminderSettingsAdapterItem item);

        void changeActiveState(@NonNull ReminderSettingsAdapterItem item, boolean isActive);

        void modifyReminderTime(@NonNull ReminderSettingsAdapterItem reminderSettingsItem, @NonNull RemindersAdapterItem reminderItem);
    }
}
