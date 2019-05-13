package com.aptatek.pkulab.domain.interactor;

import android.app.Application;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.aptatek.pkulab.domain.interactor.remindersettings.ReminderInteractor;
import com.aptatek.pkulab.domain.model.Reminder;
import com.aptatek.pkulab.domain.model.ReminderDay;
import com.aptatek.pkulab.domain.model.ReminderScheduleType;
import com.aptatek.pkulab.injection.component.DaggerAndroidTestComponent;
import com.aptatek.pkulab.injection.module.ApplicationModule;
import com.aptatek.pkulab.injection.module.ReminderModule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.observers.TestObserver;

/**
 * @test.layer Domain / Interactor
 * @test.feature Reminder Settings
 * @test.type Instrumented unit tests
 */
@RunWith(AndroidJUnit4.class)
public class ReminderInteractorTest {

    @Inject
    ReminderInteractor reminderInteractor;

    /**
     * Setting up the required modules and initialize reminder days in database.
     */
    @Before
    public void setUp() throws Exception {
        DaggerAndroidTestComponent.builder()
                .applicationModule(new ApplicationModule(((Application) InstrumentationRegistry.getTargetContext().getApplicationContext())))
                .build()
                .plus(new ReminderModule())
                .inject(this);

        final TestObserver testInit = reminderInteractor.initializeDays().test();

        testInit.await();

        testInit.assertNoErrors();
        testInit.assertComplete();

    }

    /**
     * Fetch reminder days from database
     *
     * @test.expected The stream completely emit without any error. Assert the emitted list is empty or not.
     */
    @Test
    public void testFetchDaysAfterInitialization() throws Exception {
        final TestObserver<List<ReminderDay>> testDays = reminderInteractor.listReminderDays().test();
        testDays.await();
        testDays.assertNoErrors();
        testDays.assertComplete();
        testDays.assertValueAt(0, value -> !value.isEmpty());
    }

    /**
     * Testing reminder day state update in db.
     *
     * @test.expected The stream completely emit without any error. Assert reminder day at id '1' active state successfully set to true.
     */
    @Test
    public void testUpdateReminderDayActiveState() throws Exception {
        final TestObserver testUpdate = reminderInteractor.updateReminderDayActiveState(1, true).test();

        testUpdate.await();

        testUpdate.assertNoErrors();
        testUpdate.assertComplete();

        final TestObserver<List<ReminderDay>> testDays = reminderInteractor.listReminderDays().test();
        testDays.await();
        testDays.assertNoErrors();
        testDays.assertComplete();
        testDays.assertValueAt(0, value -> value.get(0).isActive());
    }

    /**
     * Testing reminder inserted into db.
     *
     * @test.expected The stream completely emit without any error. Fetch reminder day and assert reminder list size.
     */
    @Test
    public void testInsertReminderToDb() throws Exception {
        final Reminder reminder = Reminder.builder()
                .setHour(10)
                .setMinute(10)
                .setId(UUID.randomUUID().toString())
                .setWeekDay(1)
                .setReminderScheduleType(ReminderScheduleType.WEEKLY)
                .build();

        final TestObserver testInsert = reminderInteractor.insertReminder(reminder).test();
        testInsert.await();
        testInsert.assertNoErrors();
        testInsert.assertComplete();

        final TestObserver<List<Reminder>> testReminders = reminderInteractor.listReminders(1).test();
        testReminders.await();
        testReminders.assertNoErrors();
        testReminders.assertComplete();
        testReminders.assertValueAt(0, reminders -> !reminders.isEmpty());
    }

    /**
     * Testing reminder inserted into Alarm Manager.
     *
     * @test.expected The stream completely emit without any error.
     */
    @Test
    public void testInsertReminderToAlarmManager() throws Exception {
        final TestObserver testObserver = reminderInteractor.insertReminder(1, 10, 10, ReminderScheduleType.WEEKLY).test();
        testObserver.await();
        testObserver.assertNoErrors();
        testObserver.assertComplete();
    }

    /**
     * Testing reminder deleted from db.
     *
     * @test.expected The stream completely emit without any error. Fetch reminder day and assert reminder list size.
     */
    @Test
    public void testDeleteReminderFromDb() throws Exception {
        final Reminder reminder = Reminder.builder()
                .setHour(10)
                .setMinute(10)
                .setId(UUID.randomUUID().toString())
                .setWeekDay(1)
                .setReminderScheduleType(ReminderScheduleType.WEEKLY)
                .build();

        final TestObserver testInsert = reminderInteractor.insertReminder(reminder).test();
        testInsert.await();
        testInsert.assertNoErrors();
        testInsert.assertComplete();

        final TestObserver<List<Reminder>> testReminders = reminderInteractor.listReminders(1).test();
        testReminders.await();
        testReminders.assertNoErrors();
        testReminders.assertComplete();
        testReminders.assertValueAt(0, reminders -> !reminders.isEmpty());

        final TestObserver testDelete = reminderInteractor.deleteReminder(reminder.getId()).test();
        testDelete.await();
        testDelete.assertNoErrors();
        testDelete.assertComplete();

        final TestObserver<List<Reminder>> testRemindersAfterDelete = reminderInteractor.listReminders(1).test();
        testRemindersAfterDelete.await();
        testRemindersAfterDelete.assertNoErrors();
        testRemindersAfterDelete.assertComplete();
        testRemindersAfterDelete.assertValueAt(0, Collection::isEmpty);
    }

    /**
     * Testing reminder inserted from Alarm Manager.
     *
     * @test.expected The stream completely emit without any error. Fetch reminder day and assert reminder list size.
     */
    @Test
    public void testDeleteReminderFromAlarmManager() throws Exception {
        final TestObserver testObserver = reminderInteractor.deleteReminder(1, 10, 10).test();
        testObserver.await();
        testObserver.assertNoErrors();
        testObserver.assertComplete();
    }

    /**
     * Testing reminder updated in db.
     *
     * @test.expected The stream completely emit without any error. Fetch reminder day and assert reminder item.
     */
    @Test
    public void testUpdateReminderInDb() throws Exception {
        final Reminder reminder = Reminder.builder()
                .setHour(10)
                .setMinute(10)
                .setId(UUID.randomUUID().toString())
                .setWeekDay(1)
                .setReminderScheduleType(ReminderScheduleType.WEEKLY)
                .build();

        final TestObserver testInsert = reminderInteractor.insertReminder(reminder).test();
        testInsert.await();
        testInsert.assertNoErrors();
        testInsert.assertComplete();

        final TestObserver<List<Reminder>> testReminders = reminderInteractor.listReminders(1).test();
        testReminders.await();
        testReminders.assertNoErrors();
        testReminders.assertComplete();
        testReminders.assertValueAt(0, reminders
                -> !reminders.isEmpty()
                && reminders.get(0).getHour() == 10
                && reminders.get(0).getMinute() == 10);

        final TestObserver testUpdate = reminderInteractor.updateReminder(reminder.getId(), 11, 11, ReminderScheduleType.WEEKLY).test();
        testUpdate.await();
        testUpdate.assertNoErrors();
        testUpdate.assertComplete();

        final TestObserver<List<Reminder>> testRemindersAfterUpdate = reminderInteractor.listReminders(1).test();
        testRemindersAfterUpdate.await();
        testRemindersAfterUpdate.assertNoErrors();
        testRemindersAfterUpdate.assertComplete();
        testRemindersAfterUpdate.assertValueAt(0, reminders
                -> !reminders.isEmpty()
                && reminders.get(0).getHour() == 11
                && reminders.get(0).getMinute() == 11);
    }

    /**
     * Testing reminder updated in Alarm Manager.
     *
     * @test.expected The stream completely emit without any error. Fetch reminder day and assert reminder list size.
     */
    @Test
    public void testUpdateReminderInAlarmManager() throws Exception {
        final TestObserver testObserver = reminderInteractor.updateReminder(1, 10, 10, ReminderScheduleType.WEEKLY).test();
        testObserver.await();
        testObserver.assertNoErrors();
        testObserver.assertComplete();
    }
}