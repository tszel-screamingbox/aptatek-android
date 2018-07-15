package com.aptatek.aptatek.injection.module;

import android.app.NotificationManager;
import android.content.Context;

import com.aptatek.aptatek.device.ReminderNotificationFactoryImpl;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.interactor.remindersettings.ReminderNotificationFactory;
import com.aptatek.aptatek.injection.qualifier.ApplicationContext;

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
