package com.aptatek.aptatek.injection.component;

import com.aptatek.aptatek.AlarmReceiver;
import com.aptatek.aptatek.injection.module.ApplicationModule;

import dagger.Component;

@Component(modules = {ApplicationModule.class})
public interface BroadcastReceiverComponent {

    void inject(AlarmReceiver alarmReceiver);
}
