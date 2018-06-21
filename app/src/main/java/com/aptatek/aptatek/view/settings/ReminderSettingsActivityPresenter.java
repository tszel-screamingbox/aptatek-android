package com.aptatek.aptatek.view.settings;

import android.support.annotation.NonNull;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.ResourceInteractor;
import com.aptatek.aptatek.domain.interactor.remindersettings.ReminderInteractor;
import com.aptatek.aptatek.domain.model.Reminder;
import com.aptatek.aptatek.domain.model.ReminderDay;
import com.aptatek.aptatek.util.Constants;
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

    public void addNewReminder(@NonNull final ReminderSettingsAdapterItem item, final int hour, final int minute) {
        if (checkReminderExistence(item, hour, minute)) return;

        final String id = UUID.randomUUID().toString();
        final String reminderTime = getReminderTime(hour, minute);
        item.getReminders().add(new RemindersAdapterItem(
                id,
                reminderTime,
                true,
                hour,
                minute));

        ifViewAttached(view -> view.addReminder(item));

        reminderInteractor.setReminder(item.getWeekDay(), hour, minute);

        insertReminder(item, hour, minute, id);
    }

    public void changeActiveState(@NonNull final ReminderSettingsAdapterItem item, final boolean isActive) {
        item.setActive(isActive);

        for (RemindersAdapterItem reminderItem : item.getReminders()) {
            reminderItem.setActive(isActive);

            if (item.isActive()) {
                reminderInteractor.setReminder(item.getWeekDay(), reminderItem.getHour(), reminderItem.getMinute());
            } else {
                reminderInteractor.deleteReminder(item.getWeekDay(), reminderItem.getHour(), reminderItem.getMinute());
            }
        }

        ifViewAttached(view -> view.changeActiveState(item));

        updateReminderDayActiveState(item, isActive);
    }

    public void deleteReminder(@NonNull final ReminderSettingsAdapterItem reminderSettingsItem, final @NonNull RemindersAdapterItem reminderItem) {
        reminderSettingsItem.getReminders().remove(reminderItem);
        ifViewAttached(view -> view.deleteReminder(reminderSettingsItem));

        deleteReminder(reminderItem);

        reminderInteractor.deleteReminder(reminderSettingsItem.getWeekDay(), reminderItem.getHour(), reminderItem.getMinute());
    }

    public void modifyReminder(@NonNull final ReminderSettingsAdapterItem reminderSettingsItem, final @NonNull RemindersAdapterItem reminderItem, final int hour, final int minute) {
        if (checkReminderExistence(reminderSettingsItem, hour, minute)) return;

        reminderItem.setTime(getReminderTime(hour, minute));
        reminderItem.setHour(hour);
        reminderItem.setMinute(minute);
        reminderSettingsItem.getReminders().set(reminderSettingsItem.getReminders().indexOf(reminderItem), reminderItem);
        ifViewAttached(view -> view.modifyReminder(reminderSettingsItem));

        updateReminder(reminderItem);

        reminderInteractor.updateReminder(reminderSettingsItem.getWeekDay(), reminderItem.getHour(), reminderItem.getMinute());
    }

    private void insertReminder(@NonNull final ReminderSettingsAdapterItem item, final int hour, final int minute, final String id) {
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
                    public void onSubscribe(final Disposable d) {
                        Timber.d("");
                    }

                    @Override
                    public void onComplete() {
                        Timber.d("");
                    }

                    @Override
                    public void onError(final Throwable e) {
                        Timber.d("");
                    }
                });
    }

    private void updateReminderDayActiveState(@NonNull final ReminderSettingsAdapterItem item, final boolean isActive) {
        reminderInteractor.updateReminderDayActiveState(item.getWeekDay(), isActive)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(final Disposable d) {
                        Timber.d("");
                    }

                    @Override
                    public void onComplete() {
                        Timber.d("");
                    }

                    @Override
                    public void onError(final Throwable e) {
                        Timber.d("");
                    }
                });
    }

    private void deleteReminder(@NonNull final RemindersAdapterItem reminderItem) {
        reminderInteractor.deleteReminder(reminderItem.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(final Disposable d) {
                        Timber.d("");
                    }

                    @Override
                    public void onComplete() {
                        Timber.d("");
                    }

                    @Override
                    public void onError(final Throwable e) {
                        Timber.d("");
                    }
                });
    }

    private void updateReminder(@NonNull final RemindersAdapterItem reminderItem) {
        reminderInteractor.updateReminder(reminderItem.getId(), reminderItem.getHour(), reminderItem.getMinute())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(final Disposable d) {
                        Timber.d("");
                    }

                    @Override
                    public void onComplete() {
                        Timber.d("");
                    }

                    @Override
                    public void onError(final Throwable e) {
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
                    public void onSubscribe(final Disposable d) {
                        Timber.d("");
                    }

                    @Override
                    public void onSuccess(final List<ReminderDay> reminderDays) {
                        final ArrayList<ReminderSettingsAdapterItem> data = new ArrayList<>();
                        for (ReminderDay reminderDay : reminderDays) {
                            final ArrayList<RemindersAdapterItem> remindersAdapterItems = new ArrayList<>();

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
                    public void onError(final Throwable e) {
                        Timber.d("");
                    }
                });
    }

    private boolean checkReminderExistence(@NonNull final ReminderSettingsAdapterItem item, final int hour, final int minute) {
        for (RemindersAdapterItem remindersAdapterItem : item.getReminders()) {
            if (remindersAdapterItem.getHour() == hour && remindersAdapterItem.getMinute() == minute) {
                ifViewAttached(ReminderSettingsActivityView::alreadyHasReminderError);
                return true;
            }
        }
        return false;
    }

    private String getReminderTime(final int hour, final int minute) {
        final String timePeriod;

        if (hour > Constants.REMINDER_AM_OR_PM) {
            timePeriod = resourceInteractor.getStringResource(R.string.reminder_settings_pm);
        } else {
            timePeriod = resourceInteractor.getStringResource(R.string.reminder_settings_am);
        }

        final int finalHourFormat;

        if (hour > Constants.REMINDER_AM_OR_PM) {
            finalHourFormat = hour - Constants.REMINDER_HALF_DAY;
        } else {
            finalHourFormat = hour;
        }

        return resourceInteractor.getStringResource(R.string.reminder_settings_reminder_item_time_format,
                String.valueOf(finalHourFormat),
                String.valueOf(minute),
                timePeriod);
    }
}
