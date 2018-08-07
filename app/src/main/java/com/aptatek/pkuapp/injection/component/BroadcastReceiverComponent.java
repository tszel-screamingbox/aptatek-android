package com.aptatek.pkuapp.injection.component;

import com.aptatek.pkuapp.device.AlarmReceiver;
import com.aptatek.pkuapp.device.ReminderActionReceiver;
import com.aptatek.pkuapp.injection.module.ApplicationModule;
import com.aptatek.pkuapp.injection.module.ReminderModule;

import dagger.Component;

@Component(modules = {ApplicationModule.class, ReminderModule.class})
public interface BroadcastReceiverComponent {

    void inject(AlarmReceiver alarmReceiver);

    void inject(ReminderActionReceiver reminderActionReceiver);
}
