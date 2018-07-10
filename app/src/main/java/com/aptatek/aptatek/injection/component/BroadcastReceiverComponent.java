package com.aptatek.aptatek.injection.component;

import com.aptatek.aptatek.AlarmReceiver;
import com.aptatek.aptatek.injection.module.ApplicationModule;
import com.aptatek.aptatek.injection.module.ReminderModule;

import dagger.Component;

@Component(modules = {ApplicationModule.class, ReminderModule.class})
public interface BroadcastReceiverComponent {

    void inject(AlarmReceiver alarmReceiver);
}
