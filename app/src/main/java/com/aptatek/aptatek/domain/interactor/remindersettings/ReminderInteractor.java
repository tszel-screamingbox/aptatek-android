package com.aptatek.aptatek.domain.interactor.remindersettings;

import com.aptatek.aptatek.data.AptatekDatabase;
import com.aptatek.aptatek.data.dao.ReminderDao;
import com.aptatek.aptatek.data.dao.ReminderDayDao;
import com.aptatek.aptatek.data.mapper.ReminderDayMapper;
import com.aptatek.aptatek.data.mapper.ReminderMapper;
import com.aptatek.aptatek.device.AlarmManager;
import com.aptatek.aptatek.domain.model.Reminder;
import com.aptatek.aptatek.domain.model.ReminderDay;

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
    public ReminderInteractor(AptatekDatabase aptatekDatabase, ReminderDayMapper reminderDayMapper, ReminderMapper reminderMapper, AlarmManager alarmManager) {
        this.reminderDayDao = aptatekDatabase.getReminderDayDao();
        this.reminderDayMapper = reminderDayMapper;
        this.reminderMapper = reminderMapper;
        this.reminderDao = aptatekDatabase.getReminderDao();
        this.alarmManager = alarmManager;
    }

    public Single<List<ReminderDay>> listReminderDays() {
        return reminderDayDao.getReminderDays()
                .map(reminderDayMapper::toDomainList)
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

    public Observable<List<Reminder>> listReminders(int weekDay) {
        return reminderDao.getReminders(weekDay)
                .map(reminderMapper::toDomainList)
                .toObservable();
    }

    public Completable updateReminderDayActiveState(int id, Boolean active) {
        return Completable.fromAction(() -> reminderDayDao.updateReminderDayActiveState(id, active));
    }

    public Completable insertReminder(Reminder reminder) {
        return Completable.fromAction(() -> reminderDao.insert(reminderMapper.toData(reminder)));
    }

    public Completable updateReminder(String id, int hour, int minute) {
        return Completable.fromAction(() -> reminderDao.updateReminder(id, hour, minute));
    }

    public Completable deleteReminder(String id) {
        return Completable.fromAction(() -> reminderDao.deleteReminder(id));
    }

    public void setReminder(Long timestamp, int requestCode) {
        alarmManager.setReminder(timestamp, requestCode);
    }

    public void deleteReminder(int requestCode) {
        alarmManager.deleteReminder(requestCode);
    }

    public void updateReminder(Long timestamp, int requestCode) {
        alarmManager.updateReminder(timestamp, requestCode);
    }
}
