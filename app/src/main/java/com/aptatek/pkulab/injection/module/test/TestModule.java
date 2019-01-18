package com.aptatek.pkulab.injection.module.test;

import android.app.NotificationManager;
import android.content.Context;
import android.support.annotation.NonNull;

import com.aptatek.pkulab.device.formatter.CountdownTimeFormatterImpl;
import com.aptatek.pkulab.device.notifications.WettingNotificationFactory;
import com.aptatek.pkulab.device.notifications.WettingNotificationFactoryImpl;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.countdown.CountdownTimeFormatter;
import com.aptatek.pkulab.injection.qualifier.ApplicationContext;

import dagger.Module;
import dagger.Provides;

@Module
public class TestModule {

    @Provides
    WettingNotificationFactory provideSampleWettingNotificationFactory(@ApplicationContext final Context context,
                                                                       final ResourceInteractor resourceInteractor,
                                                                       final NotificationManager notificationManager) {
        return new WettingNotificationFactoryImpl(context, resourceInteractor, notificationManager);
    }

    @Provides
    CountdownTimeFormatter provideCountdownTimeFormatter(@NonNull final ResourceInteractor resourceInteractor) {
        return new CountdownTimeFormatterImpl(resourceInteractor);
    }

}
