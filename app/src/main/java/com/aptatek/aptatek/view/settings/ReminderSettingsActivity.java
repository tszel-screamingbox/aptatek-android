package com.aptatek.aptatek.view.settings;

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

public class ReminderSettingsActivity extends BaseActivity<ReminderSettingsActivityView, ReminderSettingsActivityPresenter>
        implements ReminderSettingsActivityView {

    @Inject
    ReminderSettingsActivityPresenter presenter;

    @Inject
    ReminderSettingsItemDecoration itemDecoration;

    @Inject
    ReminderSettingsAdapter adapter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recyclerViewDays)
    RecyclerView recyclerViewDays;

    @Override
    protected void injectActivity(ActivityComponent activityComponent) {
        getActivityComponent().inject(this);
    }

    @Override
    public int getFrameLayoutId() {
        return R.layout.activity_reminder_settings;
    }

    @NonNull
    @Override
    public ReminderSettingsActivityPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setTransitionAnimation(Animation.FADE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
            public void onAddReminderClicked(@NonNull ReminderSettingsAdapterItem item) {
                TimePickerDialog.starter(getTimePickerDialogCallback(item, null)).show(getSupportFragmentManager(), "");
            }

            @Override
            public void changeActiveState(@NonNull ReminderSettingsAdapterItem item, boolean isActive) {
                presenter.changeActiveState(item, isActive);
            }

            @Override
            public void modifyReminderTime(@NonNull ReminderSettingsAdapterItem reminderSettingsItem, @NonNull RemindersAdapterItem reminderItem) {
                TimePickerDialog.starter(reminderItem.getHour(), reminderItem.getMinute(), getTimePickerDialogCallback(reminderSettingsItem, reminderItem)).show(getSupportFragmentManager(), "");
            }
        });

        presenter.initView();
    }

    @Override
    public void presentDays(@NonNull ArrayList<ReminderSettingsAdapterItem> data) {
        adapter.setData(data);
    }

    @Override
    public void addReminder(@NonNull ReminderSettingsAdapterItem item) {
        adapter.changeAdapterItem(item);
    }

    @Override
    public void changeActiveState(@NonNull ReminderSettingsAdapterItem item) {
        adapter.changeAdapterItem(item);
    }

    @Override
    public void deleteReminder(@NonNull ReminderSettingsAdapterItem item) {
        adapter.changeAdapterItem(item);
    }

    @Override
    public void modifyReminder(@NonNull ReminderSettingsAdapterItem item) {
        adapter.changeAdapterItem(item);
    }

    @Override
    public void alreadyHasReminderError() {
        Toast.makeText(this, R.string.reminder_settings_already_has_reminder, Toast.LENGTH_LONG).show();
    }

    @NonNull
    private TimePickerDialog.TimePickerDialogCallback getTimePickerDialogCallback(@NonNull ReminderSettingsAdapterItem reminderSettingsAdapterItem, @Nullable RemindersAdapterItem remindersAdapterItem) {
        return (hourOfDay, minute) -> {
            if (remindersAdapterItem != null) {
                presenter.modifyReminder(reminderSettingsAdapterItem, remindersAdapterItem, hourOfDay, minute);
            } else {
                presenter.addNewReminder(reminderSettingsAdapterItem, hourOfDay, minute);
            }
        };
    }
}
