package com.aptatek.pkuapp.domain.manager;

import android.app.Application;
import android.app.Notification;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.domain.interactor.remindersettings.ReminderNotificationFactory;
import com.aptatek.pkuapp.injection.component.DaggerAndroidTestComponent;
import com.aptatek.pkuapp.injection.module.ApplicationModule;
import com.aptatek.pkuapp.injection.module.ReminderModule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ReminderNotificationFactoryTest {

    @Inject
    ReminderNotificationFactory reminderNotificationFactory;

    @Inject
    ResourceInteractor resourceInteractor;

    @Before
    public void setUp() throws Exception {
        DaggerAndroidTestComponent.builder()
                .applicationModule(new ApplicationModule(((Application) InstrumentationRegistry.getTargetContext().getApplicationContext())))
                .build()
                .plus(new ReminderModule())
                .inject(this);
    }

    @Test
    public void testNotificationParameters() throws Exception {
        Notification notification = reminderNotificationFactory.createReminderNotification();
        assertTrue(notification.actions.length == 3);
        assertTrue(notification.extras.get("android.title") == resourceInteractor.getStringResource(R.string.reminder_notification_title));
        assertTrue(notification.extras.get("android.text") == resourceInteractor.getStringResource(R.string.reminder_notification_message));
    }
}
