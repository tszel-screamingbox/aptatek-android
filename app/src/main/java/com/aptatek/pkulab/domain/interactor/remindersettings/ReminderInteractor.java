package com.aptatek.pkulab.domain.interactor.remindersettings;

import com.aptatek.pkulab.data.AptatekDatabase;
import com.aptatek.pkulab.data.dao.ReminderDao;
import com.aptatek.pkulab.data.dao.ReminderDayDao;
import com.aptatek.pkulab.data.mapper.ReminderDayMapper;
import com.aptatek.pkulab.data.mapper.ReminderMapper;
import com.aptatek.pkulab.data.model.ReminderDayDataModel;
import com.aptatek.pkulab.device.AlarmManager;
import com.aptatek.pkulab.domain.model.Reminder;
import com.aptatek.pkulab.domain.model.ReminderDay;
import com.aptatek.pkulab.domain.model.ReminderScheduleType;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class ReminderInteractor {

    private final ReminderDayDao reminderDayDao;
    private final ReminderDao reminderDao;
    private final ReminderDayMapper reminderDayMapper;
    private final ReminderMapper reminderMapper;
    private final AlarmManager alarmManager;

    @Inject
    public ReminderInteractor(final AptatekDatabase aptatekDatabase,
                              final ReminderDayMapper reminderDayMapper,
                              final ReminderMapper reminderMapper,
                              final AlarmManager alarmManager) {
        this.reminderDayDao = aptatekDatabase.getReminderDayDao();
        this.reminderDayMapper = reminderDayMapper;
        this.reminderMapper = reminderMapper;
        this.reminderDao = aptatekDatabase.getReminderDao();
        this.alarmManager = alarmManager;
    }

    public Single<List<ReminderDay>> listReminderDays() {
        return reminderDayDao.getReminderDays()
                .map(reminderDayMapper::mapListToDomain)
                .toObservable()
                .flatMapIterable(data -> data)
                .flatMap(reminderDay -> listReminders(reminderDay.getWeekDay())
                        .map(reminders ->
                                ReminderDay.builder()
                                        .setReminders(reminders)
                                        .setActive(reminderDay.isActive())
                                        .setWeekDay(reminderDay.getWeekDay())
                                        .build()))
                .toList();
    }

    public Completable initializeDays() {
        return Completable.fromAction(() -> {
            if (reminderDayDao.getReminderDaysCount() == 0) {
                reminderDayDao.insertAll(ReminderDayDataModel.creator());
            }
        });
    }

    public Completable updateReminderDayActiveState(final int id, final Boolean active) {
        return Completable.fromAction(() -> reminderDayDao.updateReminderDayActiveState(id, active));
    }

    public Completable insertReminder(final Reminder reminder) {
        return Completable.fromAction(() -> reminderDao.insert(reminderMapper.mapToData(reminder)));
    }

    public Completable insertReminder(final int weekDay, final int hour, final int minute, final ReminderScheduleType reminderScheduleType) {
        return Completable.fromAction(() ->
                alarmManager.setReminder(getReminderTimeStamp(weekDay, hour, minute, reminderScheduleType), weekDay + hour + minute, true));
    }

    public Completable updateReminder(final String id, final int hour, final int minute, final ReminderScheduleType reminderScheduleType) {
        return Completable.fromAction(() -> reminderDao.updateReminder(id, hour, minute, reminderMapper.scheduleDataTypeToDomain(reminderScheduleType).getCode()));
    }

    public Completable updateReminder(final int weekDay, final int hour, final int minute, final ReminderScheduleType reminderScheduleType) {
        return Completable.fromAction(() ->
                alarmManager.updateReminder(getReminderTimeStamp(weekDay, hour, minute, reminderScheduleType), weekDay + hour + minute)
        );
    }

    public Completable deleteReminder(final String id) {
        return Completable.fromAction(() -> reminderDao.deleteReminder(id));
    }

    public Completable deleteReminder(final int weekDay, final int hour, final int minute) {
        return Completable.fromAction(() ->
                alarmManager.deleteReminder(weekDay + hour + minute)
        );
    }

    public Observable<List<Reminder>> listReminders(final int weekDay) {
        return reminderDao.getReminders(weekDay)
                .map(reminderMapper::mapListToDomain)
                .toObservable();
    }

    private long getReminderTimeStamp(final int weekDay, final int hour, final int minute, final ReminderScheduleType reminderScheduleType) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_WEEK, weekDay);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (Calendar.getInstance().getTimeInMillis() > calendar.getTimeInMillis()) {
            if (reminderScheduleType == ReminderScheduleType.WEEKLY) {
                calendar.set(Calendar.WEEK_OF_MONTH, calendar.get(Calendar.WEEK_OF_MONTH) + 1);
            } else if (reminderScheduleType == ReminderScheduleType.BIWEEKLY) {
                calendar.set(Calendar.WEEK_OF_MONTH, calendar.get(Calendar.WEEK_OF_MONTH) + 2);
            } else {
                calendar.set(Calendar.MONTH, calendar.get(Calendar.WEEK_OF_MONTH) + 1);
            }
        }

        return calendar.getTimeInMillis();
    }
}
