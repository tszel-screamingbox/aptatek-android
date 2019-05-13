package com.aptatek.pkulab.view.settings.reminder.adapter;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.view.base.BaseAdapter;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

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

        ReminderSettingsViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            final FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(itemView.getContext());
            layoutManager.setFlexDirection(FlexDirection.ROW);
            layoutManager.setJustifyContent(JustifyContent.FLEX_START);

            recyclerViewReminders.setLayoutManager(layoutManager);
            recyclerViewReminders.addItemDecoration(new ReminderItemDecoration());
        }

        void bind(final ReminderSettingsAdapterItem item) {
            final RemindersAdapter remindersAdapter = new RemindersAdapter();
            recyclerViewReminders.setAdapter(remindersAdapter);
            remindersAdapter.setCallback(this);
            remindersAdapter.setData(item.getReminders());

            if (item.getReminders().isEmpty()) {
                recyclerViewReminders.setVisibility(View.GONE);
            } else {
                recyclerViewReminders.setVisibility(View.VISIBLE);
            }

            textViewDayName.setText(item.getNameOfDay());
            switchActivate.setChecked(item.isActive());
            textViewDayName.setSelected(item.isActive());

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

            itemView.setOnClickListener(v -> changeState());

            recyclerViewReminders.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                @Override
                public boolean onInterceptTouchEvent(final RecyclerView rv, final MotionEvent motionEvent) {
                    if (motionEvent.getAction() != MotionEvent.ACTION_UP) {
                        return false;
                    }
                    final View child = recyclerViewReminders.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                    if (child == null) {
                        changeState();
                    }

                    return false;
                }

                @Override
                public void onTouchEvent(final RecyclerView rv, final MotionEvent e) {

                }

                @Override
                public void onRequestDisallowInterceptTouchEvent(final boolean disallowIntercept) {

                }
            });


            switchActivate.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (callback != null && data.getCurrentList().get(getAdapterPosition()).isActive() != isChecked) {
                    callback.changeActiveState(data.getCurrentList().get(getAdapterPosition()), isChecked);
                }
            });

            textViewDayName.setOnClickListener(v -> changeState());
        }

        private void changeState() {
            if (!switchActivate.isChecked()) {
                switchActivate.setChecked(true);
            }
        }

        @Override
        public void modifyReminderTime(@NonNull final RemindersAdapterItem item) {
            if (callback != null) {
                callback.modifyReminderTime(data.getCurrentList().get(getAdapterPosition()), item);
            }
        }
    }
}
