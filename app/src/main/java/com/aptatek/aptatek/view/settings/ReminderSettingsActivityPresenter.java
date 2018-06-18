package com.aptatek.aptatek.view.settings;

import android.support.annotation.NonNull;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.ArrayList;

import javax.inject.Inject;

public class ReminderSettingsActivityPresenter extends MvpBasePresenter<ReminderSettingsActivityView> {

    private static final int halfDayConst = 12;
    private static final int amOrPmConst = 11;

    private ResourceInteractor resourceInteractor;

    @Inject
    public ReminderSettingsActivityPresenter(final ResourceInteractor resourceInteractor) {
        this.resourceInteractor = resourceInteractor;
    }

    @Override
    public void destroy() {
        resourceInteractor = null;
        super.destroy();
    }

    public void initView() {

        ArrayList<ReminderSettingsAdapterItem> data = new ArrayList<>();
        data.add(new ReminderSettingsAdapterItem(resourceInteractor.getStringResource(R.string.reminder_settings_monday)));
        data.add(new ReminderSettingsAdapterItem(resourceInteractor.getStringResource(R.string.reminder_settings_tuesday)));
        data.add(new ReminderSettingsAdapterItem(resourceInteractor.getStringResource(R.string.reminder_settings_wednesday)));
        data.add(new ReminderSettingsAdapterItem(resourceInteractor.getStringResource(R.string.reminder_settings_thursday)));
        data.add(new ReminderSettingsAdapterItem(resourceInteractor.getStringResource(R.string.reminder_settings_friday)));
        data.add(new ReminderSettingsAdapterItem(resourceInteractor.getStringResource(R.string.reminder_settings_saturday)));
        data.add(new ReminderSettingsAdapterItem(resourceInteractor.getStringResource(R.string.reminder_settings_sunday)));

        ifViewAttached(view -> view.presentDays(data));
    }

    public void addNewReminder(@NonNull ReminderSettingsAdapterItem item, int hour, int minute) {
        if (checkReminderExistence(item, hour, minute)) return;


        item.getReminders().add(new RemindersAdapterItem(
                getReminderTime(hour, minute),
                true,
                hour,
                minute));

        ifViewAttached(view -> view.addReminder(item));
    }

    public void changeActiveState(@NonNull ReminderSettingsAdapterItem item, boolean isActive) {
        item.setActive(isActive);

        for (RemindersAdapterItem reminderItem : item.getReminders()) {
            reminderItem.setActive(isActive);
        }

        ifViewAttached(view -> view.changeActiveState(item));
    }

    public void deleteReminder(@NonNull ReminderSettingsAdapterItem reminderSettingsItem, @NonNull RemindersAdapterItem reminderItem) {
        reminderSettingsItem.getReminders().remove(reminderItem);
        ifViewAttached(view -> view.deleteReminder(reminderSettingsItem));
    }

    public void modifyReminder(@NonNull ReminderSettingsAdapterItem reminderSettingsItem, @NonNull RemindersAdapterItem reminderItem, int hour, int minute) {
        if (checkReminderExistence(reminderSettingsItem, hour, minute)) return;

        reminderItem.setTime(getReminderTime(hour, minute));
        reminderItem.setHour(hour);
        reminderItem.setMinute(minute);
        reminderSettingsItem.getReminders().set(reminderSettingsItem.getReminders().indexOf(reminderItem), reminderItem);
        ifViewAttached(view -> view.modifyReminder(reminderSettingsItem));
    }

    private boolean checkReminderExistence(@NonNull ReminderSettingsAdapterItem item, int hour, int minute) {
        for (RemindersAdapterItem remindersAdapterItem : item.getReminders()) {
            if (remindersAdapterItem.getHour() == hour && remindersAdapterItem.getMinute() == minute) {
                ifViewAttached(ReminderSettingsActivityView::alreadyHasReminderError);
                return true;
            }
        }
        return false;
    }

    private String getReminderTime(int hour, int minute) {
        String timePeriod;

        if (hour > amOrPmConst) {
            timePeriod = resourceInteractor.getStringResource(R.string.reminder_settings_pm);
        } else {
            timePeriod = resourceInteractor.getStringResource(R.string.reminder_settings_am);
        }

        int finalHourFormat;

        if (hour > amOrPmConst) {
            finalHourFormat = hour - halfDayConst;
        } else {
            finalHourFormat = hour;
        }

        return resourceInteractor.getStringResource(R.string.reminder_settings_reminder_item_time_format,
                String.valueOf(finalHourFormat),
                String.valueOf(minute),
                timePeriod);
    }
}
