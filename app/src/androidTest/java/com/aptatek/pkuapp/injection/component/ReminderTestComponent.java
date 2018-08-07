package com.aptatek.pkuapp.injection.component;

import com.aptatek.pkuapp.domain.interactor.ReminderInteractorTest;
import com.aptatek.pkuapp.domain.manager.ReminderNotificationFactoryTest;
import com.aptatek.pkuapp.injection.module.ReminderModule;

import dagger.Subcomponent;

@Subcomponent(modules = ReminderModule.class)
public interface ReminderTestComponent {

    void inject(ReminderNotificationFactoryTest reminderNotificationFactoryTest);

    void inject(ReminderInteractorTest reminderInteractorTest);
}
