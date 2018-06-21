package com.aptatek.aptatek.view.settings.reminder;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.view.base.BaseActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReminderSettings extends BaseActivity<ReminderSettingsView, ReminderSettingsPresenter>
        implements ReminderSettingsView {

    @Inject
    ReminderSettingsPresenter presenter;

    @Inject
    ReminderSettingsItemDecoration itemDecoration;

    @Inject
    ReminderSettingsAdapter adapter;

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
    public void onBackPressed() {
        super.onBackPressed();
        setTransitionAnimation(Animation.FADE);
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_settings);
        ButterKnife.bind(this);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setTitle(R.string.reminder_settings_toolbar_title);

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        recyclerViewDays.setAdapter(adapter);
        recyclerViewDays.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDays.addItemDecoration(itemDecoration);

        adapter.setCallback(new ReminderSettingsAdapter.ReminderSettingsAdapterCallback() {
            @Override
            public void onAddReminderClicked(@NonNull final ReminderSettingsAdapterItem item) {
                TimePickerDialog.starter(getTimePickerDialogCallback(item, null)).show(getSupportFragmentManager(), "");
            }

            @Override
            public void changeActiveState(@NonNull final ReminderSettingsAdapterItem item, final boolean isActive) {
                presenter.changeActiveState(item, isActive);
            }

            @Override
            public void modifyReminderTime(@NonNull final ReminderSettingsAdapterItem reminderSettingsItem, final @NonNull RemindersAdapterItem reminderItem) {
                TimePickerDialog.starter(reminderItem.getHour(), reminderItem.getMinute(), getTimePickerDialogCallback(reminderSettingsItem, reminderItem)).show(getSupportFragmentManager(), "");
            }
        });

        presenter.initView();
    }

    @Override
    public void presentDays(@NonNull final ArrayList<ReminderSettingsAdapterItem> data) {
        adapter.setData(data);
    }

    @Override
    public void addReminder(@NonNull final ReminderSettingsAdapterItem item) {
        adapter.changeAdapterItem(item);
    }

    @Override
    public void changeActiveState(@NonNull final ReminderSettingsAdapterItem item) {
        adapter.changeAdapterItem(item);
    }

    @Override
    public void deleteReminder(@NonNull final ReminderSettingsAdapterItem item) {
        adapter.changeAdapterItem(item);
    }

    @Override
    public void modifyReminder(@NonNull final ReminderSettingsAdapterItem item) {
        adapter.changeAdapterItem(item);
    }

    @Override
    public void alreadyHasReminderError() {
        Toast.makeText(this, R.string.reminder_settings_already_has_reminder, Toast.LENGTH_LONG).show();
    }

    @NonNull
    private TimePickerDialog.TimePickerDialogCallback getTimePickerDialogCallback(@NonNull final ReminderSettingsAdapterItem reminderSettingsAdapterItem, @Nullable final RemindersAdapterItem remindersAdapterItem) {
        return new TimePickerDialog.TimePickerDialogCallback() {
            @Override
            public void done(final int hourOfDay, final int minute) {
                if (remindersAdapterItem != null) {
                    presenter.modifyReminder(reminderSettingsAdapterItem, remindersAdapterItem, hourOfDay, minute);
                } else {
                    presenter.addNewReminder(reminderSettingsAdapterItem, hourOfDay, minute);
                }
            }

            @Override
            public void delete() {
                if (remindersAdapterItem != null) {
                    presenter.deleteReminder(reminderSettingsAdapterItem, remindersAdapterItem);
                }
            }
        };
    }
}
