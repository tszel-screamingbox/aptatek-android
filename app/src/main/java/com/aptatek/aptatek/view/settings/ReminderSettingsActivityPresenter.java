package com.aptatek.aptatek.view.settings;

import android.support.annotation.NonNull;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.ResourceInteractor;
import com.aptatek.aptatek.domain.interactor.remindersettings.ReminderInteractor;
import com.aptatek.aptatek.domain.model.Reminder;
import com.aptatek.aptatek.domain.model.ReminderDay;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class ReminderSettingsActivityPresenter extends MvpBasePresenter<ReminderSettingsActivityView> {

    private static final int halfDayConst = 12;
    private static final int amOrPmConst = 11;

    private ResourceInteractor resourceInteractor;
    private ReminderInteractor reminderInteractor;

    @Inject
    public ReminderSettingsActivityPresenter(final ResourceInteractor resourceInteractor,
                                             final ReminderInteractor reminderInteractor) {
        this.resourceInteractor = resourceInteractor;
        this.reminderInteractor = reminderInteractor;
    }

    @Override
    public void destroy() {
        resourceInteractor = null;
        super.destroy();
    }

    public void initView() {

        listReminderDays();
    }

    public void addNewReminder(@NonNull ReminderSettingsAdapterItem item, int hour, int minute) {
        if (checkReminderExistence(item, hour, minute)) return;

        String id = UUID.randomUUID().toString();
        String reminderTime = getReminderTime(hour, minute);
        item.getReminders().add(new RemindersAdapterItem(
                id,
                reminderTime,
                true,
                hour,
                minute));

        ifViewAttached(view -> view.addReminder(item));

        insertReminder(item, hour, minute, id);
    }

    public void changeActiveState(@NonNull ReminderSettingsAdapterItem item, boolean isActive) {
        item.setActive(isActive);

        for (RemindersAdapterItem reminderItem : item.getReminders()) {
            reminderItem.setActive(isActive);
        }

        ifViewAttached(view -> view.changeActiveState(item));

        updateReminderDayActiveState(item, isActive);
    }

    public void deleteReminder(@NonNull ReminderSettingsAdapterItem reminderSettingsItem, @NonNull RemindersAdapterItem reminderItem) {
        reminderSettingsItem.getReminders().remove(reminderItem);
        ifViewAttached(view -> view.deleteReminder(reminderSettingsItem));

        deleteReminder(reminderItem);
    }

    public void modifyReminder(@NonNull ReminderSettingsAdapterItem reminderSettingsItem, @NonNull RemindersAdapterItem reminderItem, int hour, int minute) {
        if (checkReminderExistence(reminderSettingsItem, hour, minute)) return;

        reminderItem.setTime(getReminderTime(hour, minute));
        reminderItem.setHour(hour);
        reminderItem.setMinute(minute);
        reminderSettingsItem.getReminders().set(reminderSettingsItem.getReminders().indexOf(reminderItem), reminderItem);
        ifViewAttached(view -> view.modifyReminder(reminderSettingsItem));

        updateReminder(reminderItem);
    }

    private void insertReminder(@NonNull ReminderSettingsAdapterItem item, int hour, int minute, String id) {
        reminderInteractor.insertReminder(Reminder.builder()
                .setId(id)
                .setHour(hour)
                .setMinute(minute)
                .setWeekDay(item.getWeekDay())
                .build())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Timber.d("");
                    }

                    @Override
                    public void onComplete() {
                        Timber.d("");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.d("");
                    }
                });
    }

    private void updateReminderDayActiveState(@NonNull ReminderSettingsAdapterItem item, boolean isActive) {
        reminderInteractor.updateReminderDayActiveState(item.getWeekDay(), isActive)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Timber.d("");
                    }

                    @Override
                    public void onComplete() {
                        Timber.d("");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.d("");
                    }
                });
    }

    private void deleteReminder(@NonNull RemindersAdapterItem reminderItem) {
        reminderInteractor.deleteReminder(reminderItem.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Timber.d("");
                    }

                    @Override
                    public void onComplete() {
                        Timber.d("");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.d("");
                    }
                });
    }

    private void updateReminder(@NonNull RemindersAdapterItem reminderItem) {
        reminderInteractor.updateReminder(reminderItem.getId(), reminderItem.getHour(), reminderItem.getMinute())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Timber.d("");
                    }

                    @Override
                    public void onComplete() {
                        Timber.d("");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.d("");
                    }
                });
    }

    private void listReminderDays() {
        reminderInteractor.listReminderDays()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<List<ReminderDay>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Timber.d("");
                    }

                    @Override
                    public void onSuccess(List<ReminderDay> reminderDays) {
                        ArrayList<ReminderSettingsAdapterItem> data = new ArrayList<>();
                        for (ReminderDay reminderDay : reminderDays) {
                            ArrayList<RemindersAdapterItem> remindersAdapterItems = new ArrayList<>();

                            for (Reminder reminder : reminderDay.getReminders()) {
                                remindersAdapterItems.add(
                                        new RemindersAdapterItem(
                                                reminder.getId(),
                                                getReminderTime(reminder.getHour(), reminder.getMinute()),
                                                reminderDay.isActive(),
                                                reminder.getHour(),
                                                reminder.getMinute()));
                            }

                            data.add(
                                    new ReminderSettingsAdapterItem(
                                            reminderDay.getWeekDay(),
                                            new DateFormatSymbols().getWeekdays()[reminderDay.getWeekDay()],
                                            reminderDay.isActive(),
                                            remindersAdapterItems));
                        }

                        ifViewAttached(view -> view.presentDays(data));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.d("");
                    }
                });
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
