package com.aptatek.aptatek.injection.component;

import com.aptatek.aptatek.domain.interactor.ReminderInteractorTest;
import com.aptatek.aptatek.domain.manager.ReminderNotificationFactoryTest;
import com.aptatek.aptatek.injection.module.ReminderModule;

import dagger.Subcomponent;

@Subcomponent(modules = ReminderModule.class)
public interface ReminderTestComponent {

    void inject(ReminderNotificationFactoryTest reminderNotificationFactoryTest);

    void inject(ReminderInteractorTest reminderInteractorTest);
}
