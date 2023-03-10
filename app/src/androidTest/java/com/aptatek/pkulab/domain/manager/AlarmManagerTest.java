package com.aptatek.pkulab.domain.manager;

import android.app.PendingIntent;
import android.content.Intent;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.aptatek.pkulab.device.AlarmManager;
import com.aptatek.pkulab.device.AlarmReceiver;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class AlarmManagerTest {

    private AlarmManager alarmManager;

    @Before
    public void setUp() throws Exception {
        alarmManager = new AlarmManager(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void testAlarmScheduled() throws Exception {
        alarmManager.setReminder(System.currentTimeMillis(), 102030, false);

        boolean alarmUp = (PendingIntent.getBroadcast(InstrumentationRegistry.getTargetContext(),
                102030, new Intent(InstrumentationRegistry.getTargetContext(), AlarmReceiver.class), PendingIntent.FLAG_NO_CREATE) != null);

        assertTrue(alarmUp);
    }
}
