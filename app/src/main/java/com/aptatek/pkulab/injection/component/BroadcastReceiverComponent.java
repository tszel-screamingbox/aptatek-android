package com.aptatek.pkulab.injection.component;

import com.aptatek.pkulab.device.AlarmReceiver;
import com.aptatek.pkulab.device.BootCompletedReceiver;
import com.aptatek.pkulab.device.ReminderActionReceiver;
import com.aptatek.pkulab.injection.module.ReminderModule;
import com.aptatek.pkulab.injection.scope.BroadcastReceiverScope;

import dagger.Component;

@BroadcastReceiverScope
@Component(dependencies = ApplicationComponent.class, modules = {ReminderModule.class})
public interface BroadcastReceiverComponent {

    void inject(AlarmReceiver alarmReceiver);

    void inject(ReminderActionReceiver reminderActionReceiver);

    void inject(BootCompletedReceiver bootCompletedReceiver);
}
