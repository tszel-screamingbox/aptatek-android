package com.aptatek.pkulab.view.settings.reminder;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.remindersettings.ReminderInteractor;
import com.aptatek.pkulab.domain.model.Reminder;
import com.aptatek.pkulab.domain.model.ReminderDay;
import com.aptatek.pkulab.domain.model.ReminderScheduleType;
import com.aptatek.pkulab.util.Constants;
import com.aptatek.pkulab.view.settings.reminder.adapter.ReminderSettingsAdapterItem;
import com.aptatek.pkulab.view.settings.reminder.adapter.RemindersAdapterItem;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.apache.commons.text.WordUtils;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class ReminderSettingsPresenter extends MvpBasePresenter<ReminderSettingsView> {

    private final ResourceInteractor resourceInteractor;
    private final ReminderInteractor reminderInteractor;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public ReminderSettingsPresenter(final ResourceInteractor resourceInteractor,
                                     final ReminderInteractor reminderInteractor) {
        this.resourceInteractor = resourceInteractor;
        this.reminderInteractor = reminderInteractor;
    }

    @Override
    public void detachView() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }

        super.detachView();
    }

    public void initView() {
        listReminderDays();
    }

    public void addNewReminder(@NonNull final List<ReminderSettingsAdapterItem> data,
                               @NonNull final ReminderSettingsAdapterItem item,
                               final int hour,
                               final int minute,
                               final ReminderScheduleType reminderScheduleType) {

        final List<ReminderSettingsAdapterItem> reminderSettingsAdapterItems = new ArrayList<>(data);
        final List<RemindersAdapterItem> remindersAdapterItems = new ArrayList<>(item.getReminders());

        if (checkReminderExistence(item, hour, minute, reminderScheduleType)) return;

        final String id = UUID.randomUUID().toString();
        final String reminderTime = getReminderTime(hour, minute, reminderScheduleType);

        remindersAdapterItems.add(
                RemindersAdapterItem.builder()
                        .setId(id)
                        .setTime(reminderTime)
                        .setReminderScheduleType(reminderScheduleType)
                        .setActive(true)
                        .setHour(hour)
                        .setMinute(minute)
                        .build());

        reminderSettingsAdapterItems.set(reminderSettingsAdapterItems.indexOf(item),
                ReminderSettingsAdapterItem.builder()
                        .setWeekDay(item.getWeekDay())
                        .setNameOfDay(item.getNameOfDay())
                        .setActive(item.isActive())
                        .setReminders(remindersAdapterItems)
                        .build());

        ifViewAttached(view -> view.addReminder(reminderSettingsAdapterItems));

        compositeDisposable.add(
                reminderInteractor
                        .insertReminder(item.getWeekDay(), hour, minute, reminderScheduleType)
                        .subscribe());

        insertReminder(item, hour, minute, id, reminderScheduleType);
    }

    public void changeActiveState(@NonNull final List<ReminderSettingsAdapterItem> data,
                                  @NonNull final ReminderSettingsAdapterItem item,
                                  final boolean isActive) {

        final ReminderSettingsAdapterItem.Builder settingsAdapterItemBuilder = item.toBuilder();
        settingsAdapterItemBuilder.setActive(isActive);

        final List<ReminderSettingsAdapterItem> reminderSettingsAdapterItems = new ArrayList<>(data);
        final List<RemindersAdapterItem> remindersAdapterItems = new ArrayList<>(item.getReminders());

        for (RemindersAdapterItem reminderItem : remindersAdapterItems) {

            if (item.isActive()) {
                compositeDisposable.add(
                        reminderInteractor
                                .insertReminder(item.getWeekDay(), reminderItem.getHour(), reminderItem.getMinute(), reminderItem.getReminderScheduleType())
                                .subscribe());
            } else {
                compositeDisposable.add(
                        reminderInteractor
                                .deleteReminder(item.getWeekDay(), reminderItem.getHour(), reminderItem.getMinute())
                                .subscribe());
            }

            remindersAdapterItems.set(
                    remindersAdapterItems.indexOf(reminderItem),
                    reminderItem.toBuilder()
                            .setActive(isActive)
                            .build());
        }

        settingsAdapterItemBuilder.setReminders(remindersAdapterItems);

        final ReminderSettingsAdapterItem reminderSettingsAdapterItem = settingsAdapterItemBuilder.build();
        reminderSettingsAdapterItems.set(reminderSettingsAdapterItems.indexOf(item), reminderSettingsAdapterItem);

        ifViewAttached(view -> {
            view.changeActiveState(reminderSettingsAdapterItems);

            if (isActive) {
                view.showTimePickerDialog(reminderSettingsAdapterItem);
            }
        });

        updateReminderDayActiveState(settingsAdapterItemBuilder.build(), isActive);
    }

    public void deleteReminder(@NonNull final List<ReminderSettingsAdapterItem> data,
                               @NonNull final ReminderSettingsAdapterItem reminderSettingsItem,
                               @NonNull final RemindersAdapterItem reminderItem) {

        final List<ReminderSettingsAdapterItem> reminderSettingsAdapterItems = new ArrayList<>(data);
        final List<RemindersAdapterItem> remindersAdapterItems = new ArrayList<>(reminderSettingsItem.getReminders());

        remindersAdapterItems.remove(reminderItem);

        final ReminderSettingsAdapterItem.Builder builder = reminderSettingsItem.toBuilder()
                .setReminders(remindersAdapterItems)
                .setActive(!remindersAdapterItems.isEmpty());

        reminderSettingsAdapterItems.set(reminderSettingsAdapterItems.indexOf(reminderSettingsItem),
                builder.build());

        ifViewAttached(view -> view.deleteReminder(reminderSettingsAdapterItems));

        deleteReminder(reminderItem);

        if (remindersAdapterItems.isEmpty()) {
            updateReminderDayActiveState(reminderSettingsItem, false);
        }

        compositeDisposable.add(reminderInteractor
                .deleteReminder(reminderSettingsItem.getWeekDay(), reminderItem.getHour(), reminderItem.getMinute())
                .subscribe());
    }

    private void deleteReminder(@NonNull final RemindersAdapterItem reminderItem) {
        compositeDisposable.add(reminderInteractor.deleteReminder(reminderItem.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe());
    }

    public void modifyReminder(@NonNull final List<ReminderSettingsAdapterItem> data,
                               @NonNull final ReminderSettingsAdapterItem reminderSettingsItem,
                               final @NonNull RemindersAdapterItem reminderItem,
                               final int hour,
                               final int minute,
                               final ReminderScheduleType reminderScheduleType) {

        final ArrayList<ReminderSettingsAdapterItem> reminderSettingsAdapterItems = new ArrayList<>(data);
        final ArrayList<RemindersAdapterItem> remindersAdapterItems = new ArrayList<>(reminderSettingsItem.getReminders());

        if (checkReminderExistence(reminderSettingsItem, hour, minute, reminderScheduleType))
            return;

        final RemindersAdapterItem reminderItemCopy = reminderItem.toBuilder()
                .setHour(hour)
                .setMinute(minute)
                .setTime(getReminderTime(hour, minute, reminderScheduleType))
                .setReminderScheduleType(reminderScheduleType)
                .build();

        remindersAdapterItems.set(
                reminderSettingsItem.getReminders().indexOf(reminderItem),
                reminderItemCopy);

        reminderSettingsAdapterItems.set(reminderSettingsAdapterItems.indexOf(reminderSettingsItem),
                reminderSettingsItem
                        .toBuilder()
                        .setReminders(remindersAdapterItems)
                        .build());

        ifViewAttached(view -> view.modifyReminder(reminderSettingsAdapterItems));

        updateReminder(reminderItemCopy, reminderScheduleType);

        compositeDisposable.add(reminderInteractor
                .updateReminder(reminderSettingsItem.getWeekDay(), reminderItemCopy.getHour(), reminderItemCopy.getMinute(), reminderScheduleType)
                .subscribe());
    }

    private void insertReminder(@NonNull final ReminderSettingsAdapterItem item,
                                final int hour,
                                final int minute,
                                final String id,
                                final ReminderScheduleType reminderScheduleType) {
        compositeDisposable.add(reminderInteractor.insertReminder(Reminder.builder()
                .setId(id)
                .setHour(hour)
                .setMinute(minute)
                .setReminderScheduleType(reminderScheduleType)
                .setWeekDay(item.getWeekDay())
                .build())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe());
    }

    private void updateReminderDayActiveState(@NonNull final ReminderSettingsAdapterItem item,
                                              final boolean isActive) {
        compositeDisposable.add(reminderInteractor.updateReminderDayActiveState(item.getWeekDay(), isActive)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe());
    }

    private void updateReminder(@NonNull final RemindersAdapterItem reminderItem, final ReminderScheduleType reminderScheduleType) {
        compositeDisposable.add(reminderInteractor.updateReminder(reminderItem.getId(), reminderItem.getHour(), reminderItem.getMinute(), reminderScheduleType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe());
    }

    private void listReminderDays() {
        reminderInteractor.listReminderDays()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<List<ReminderDay>>() {
                    @Override
                    public void onSubscribe(final Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(final List<ReminderDay> reminderDays) {
                        final ArrayList<ReminderSettingsAdapterItem> data = new ArrayList<>();
                        for (ReminderDay reminderDay : reminderDays) {
                            final ArrayList<RemindersAdapterItem> remindersAdapterItems = new ArrayList<>();

                            for (Reminder reminder : reminderDay.getReminders()) {
                                remindersAdapterItems.add(
                                        RemindersAdapterItem.builder()
                                                .setId(reminder.getId())
                                                .setTime(getReminderTime(reminder.getHour(), reminder.getMinute(), reminder.getReminderScheduleType()))
                                                .setReminderScheduleType(reminder.getReminderScheduleType())
                                                .setActive(reminderDay.isActive())
                                                .setHour(reminder.getHour())
                                                .setMinute(reminder.getMinute())
                                                .build());
                            }

                            data.add(
                                    ReminderSettingsAdapterItem.builder()
                                            .setWeekDay(reminderDay.getWeekDay())
                                            .setNameOfDay(WordUtils.capitalize(new DateFormatSymbols().getWeekdays()[reminderDay.getWeekDay()]))
                                            .setReminders(remindersAdapterItems)
                                            .setActive(reminderDay.isActive())
                                            .build());
                        }

                        ifViewAttached(view -> view.presentDays(data));
                    }

                    @Override
                    public void onError(final Throwable e) {
                        Timber.d("");
                    }
                });
    }

    private boolean checkReminderExistence(@NonNull final ReminderSettingsAdapterItem item,
                                           final int hour,
                                           final int minute,
                                           final ReminderScheduleType reminderScheduleType) {
        for (RemindersAdapterItem remindersAdapterItem : item.getReminders()) {
            if (remindersAdapterItem.getHour() == hour
                    && remindersAdapterItem.getMinute() == minute
                    && remindersAdapterItem.getReminderScheduleType() == reminderScheduleType) {
                ifViewAttached(ReminderSettingsView::showAlreadyHasReminderError);
                return true;
            }
        }
        return false;
    }

    private String getReminderTime(final int hour,
                                   final int minute,
                                   final ReminderScheduleType reminderScheduleType) {
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
                timePeriod,
                getScheduleLabel(reminderScheduleType));
    }

    private String getScheduleLabel(final ReminderScheduleType reminderScheduleType) {
        if (reminderScheduleType == ReminderScheduleType.WEEKLY) {
            return resourceInteractor.getStringResource(R.string.settings_daily_reminder_schedule_type_weekly);
        } else if (reminderScheduleType == ReminderScheduleType.MONTHLY) {
            return resourceInteractor.getStringResource(R.string.settings_daily_reminder_schedule_type_monthly);
        } else {
            return resourceInteractor.getStringResource(R.string.settings_daily_reminder_schedule_type_biweekly);
        }
    }
}
