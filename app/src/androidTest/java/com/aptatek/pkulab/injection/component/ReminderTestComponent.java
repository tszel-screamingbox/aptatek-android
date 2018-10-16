package com.aptatek.pkulab.injection.component;

import com.aptatek.pkulab.domain.interactor.ReminderInteractorTest;
import com.aptatek.pkulab.domain.manager.ReminderNotificationFactoryTest;
import com.aptatek.pkulab.injection.module.ReminderModule;

import dagger.Subcomponent;

@Subcomponent(modules = ReminderModule.class)
public interface ReminderTestComponent {

    void inject(ReminderNotificationFactoryTest reminderNotificationFactoryTest);

    void inject(ReminderInteractorTest reminderInteractorTest);
}
