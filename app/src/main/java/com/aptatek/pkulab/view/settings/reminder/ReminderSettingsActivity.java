package com.aptatek.pkulab.view.settings.reminder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.domain.manager.analytic.events.settings.Reminder;
import com.aptatek.pkulab.domain.model.ReminderScheduleType;
import com.aptatek.pkulab.injection.component.ActivityComponent;
import com.aptatek.pkulab.view.base.BaseActivity;
import com.aptatek.pkulab.view.settings.reminder.adapter.ReminderSettingsAdapter;
import com.aptatek.pkulab.view.settings.reminder.adapter.ReminderSettingsAdapterCallback;
import com.aptatek.pkulab.view.settings.reminder.adapter.ReminderSettingsAdapterItem;
import com.aptatek.pkulab.view.settings.reminder.adapter.ReminderSettingsItemDecoration;
import com.aptatek.pkulab.view.settings.reminder.adapter.RemindersAdapterItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReminderSettingsActivity extends BaseActivity<ReminderSettingsView, ReminderSettingsPresenter>
        implements ReminderSettingsView {

    public static Intent starter(@NonNull final Context context) {
        return new Intent(context, ReminderSettingsActivity.class);
    }

    @Inject
    ReminderSettingsPresenter presenter;

    @Inject
    ReminderSettingsItemDecoration itemDecoration;

    @Inject
    ReminderSettingsAdapter adapter;

    @Inject
    IAnalyticsManager analyticsManager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recyclerViewDays)
    RecyclerView recyclerViewDays;

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        getActivityComponent().inject(this);
    }

    @Override
    public int getFrameLayoutId() {
        return R.layout.activity_reminder_settings;
    }

    @NonNull
    @Override
    public ReminderSettingsPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void finish() {
        super.finish();
        setTransitionAnimation(Animation.LEFT_TO_RIGHT);
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_settings);
        ButterKnife.bind(this);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle(R.string.reminder_settings_toolbar_title);

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        recyclerViewDays.setAdapter(adapter);
        recyclerViewDays.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDays.addItemDecoration(itemDecoration);

        adapter.setCallback(new ReminderSettingsAdapterCallback() {
            @Override
            public void onAddReminderClicked(@NonNull final ReminderSettingsAdapterItem item) {
                showTimePickerDialog(item);
            }

            @Override
            public void changeActiveState(@NonNull final ReminderSettingsAdapterItem item, final boolean isActive) {
                presenter.changeActiveState(adapter.getData(), item, isActive);
            }

            @Override
            public void modifyReminderTime(@NonNull final ReminderSettingsAdapterItem reminderSettingsItem, final @NonNull RemindersAdapterItem reminderItem) {
                TimePickerDialog.createForEdit(reminderItem.getHour(),
                                reminderItem.getMinute(),
                                reminderItem.getReminderScheduleType(),
                                getTimePickerDialogCallback(reminderSettingsItem, reminderItem))
                        .show(getSupportFragmentManager(), "");
            }
        });

        presenter.initView();
    }

    @Override
    public void presentDays(@NonNull final ArrayList<ReminderSettingsAdapterItem> data) {
        adapter.setData(data);
    }

    @Override
    public void addReminder(@NonNull final List<ReminderSettingsAdapterItem> data) {
        adapter.setData(data);
    }

    @Override
    public void changeActiveState(@NonNull final List<ReminderSettingsAdapterItem> data) {
        adapter.setData(data);
    }

    @Override
    public void deleteReminder(@NonNull final List<ReminderSettingsAdapterItem> data) {
        adapter.setData(data);
    }

    @Override
    public void modifyReminder(@NonNull final List<ReminderSettingsAdapterItem> data) {
        adapter.setData(data);
    }

    @Override
    public void showTimePickerDialog(@NonNull final ReminderSettingsAdapterItem item) {
        TimePickerDialog.create(getTimePickerDialogCallback(item, null))
                .show(getSupportFragmentManager(), "");
    }


    @Override
    public void showAlreadyHasReminderError() {
        Toast.makeText(this, R.string.reminder_settings_already_has_reminder, Toast.LENGTH_LONG).show();
    }

    @NonNull
    private TimePickerDialog.TimePickerDialogCallback getTimePickerDialogCallback(@NonNull final ReminderSettingsAdapterItem settingsAdapterItem, @Nullable final RemindersAdapterItem remindersAdapterItem) {
        return new TimePickerDialog.TimePickerDialogCallback() {
            @Override
            public void done(final int hourOfDay, final int minute, final ReminderScheduleType reminderScheduleType) {
                if (remindersAdapterItem != null) {
                    analyticsManager.logEvent(new Reminder("reminder_updated", settingsAdapterItem.getNameOfDay(), reminderScheduleType));
                    presenter.modifyReminder(adapter.getData(), settingsAdapterItem, remindersAdapterItem, hourOfDay, minute, reminderScheduleType);
                } else {
                    analyticsManager.logEvent(new Reminder("reminder_added", settingsAdapterItem.getNameOfDay(), reminderScheduleType));
                    presenter.addNewReminder(adapter.getData(), settingsAdapterItem, hourOfDay, minute, reminderScheduleType);
                }
            }

            @Override
            public void delete() {
                if (remindersAdapterItem != null) {
                    analyticsManager.logEvent(new Reminder("reminder_deleted", settingsAdapterItem.getNameOfDay(), remindersAdapterItem.getReminderScheduleType()));
                    presenter.deleteReminder(adapter.getData(), settingsAdapterItem, remindersAdapterItem);
                }
            }

            @Override
            public void cancel() {
                presenter.timePickerDialogCancel(adapter.getData(), settingsAdapterItem);
            }
        };
    }
}
