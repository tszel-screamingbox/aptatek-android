package com.aptatek.pkulab.injection.module;

import android.app.NotificationManager;
import android.content.Context;

import com.aptatek.pkulab.device.ReminderNotificationFactoryImpl;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.remindersettings.ReminderNotificationFactory;
import com.aptatek.pkulab.injection.qualifier.ApplicationContext;

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
