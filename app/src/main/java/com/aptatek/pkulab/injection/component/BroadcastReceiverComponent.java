package com.aptatek.pkulab.injection.component;

import com.aptatek.pkulab.device.AlarmReceiver;
import com.aptatek.pkulab.device.BootCompletedReceiver;
import com.aptatek.pkulab.device.ReminderActionReceiver;
import com.aptatek.pkulab.injection.module.ApplicationModule;
import com.aptatek.pkulab.injection.module.DatabaseModule;
import com.aptatek.pkulab.injection.module.ReminderModule;

import dagger.Component;

@Component(modules = {ApplicationModule.class, ReminderModule.class, DatabaseModule.class})
public interface BroadcastReceiverComponent {

    void inject(AlarmReceiver alarmReceiver);

    void inject(ReminderActionReceiver reminderActionReceiver);

    void inject(BootCompletedReceiver bootCompletedReceiver);
}
