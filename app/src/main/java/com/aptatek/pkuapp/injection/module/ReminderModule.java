package com.aptatek.pkuapp.injection.module;

import android.app.NotificationManager;
import android.content.Context;

import com.aptatek.pkuapp.device.ReminderNotificationFactoryImpl;
import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.domain.interactor.remindersettings.ReminderNotificationFactory;
import com.aptatek.pkuapp.injection.qualifier.ApplicationContext;

import dagger.Module;
import dagger.Provides;

@Module
public class ReminderModule {

    @Provides
    ReminderNotificationFactory provideReminderNotificationFactory(@ApplicationContext final Context context,
                                                                   final ResourceInteractor resourceInteractor,
                                                                   final NotificationManager notificationManager) {
        return new ReminderNotificationFactoryImpl(context, resourceInteractor, notificationManager);
    }
}
