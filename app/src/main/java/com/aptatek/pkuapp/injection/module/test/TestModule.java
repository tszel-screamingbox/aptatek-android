package com.aptatek.pkuapp.injection.module.test;

import android.app.NotificationManager;
import android.content.Context;
import android.support.annotation.NonNull;

import com.aptatek.pkuapp.device.formatter.CountdownTimeFormatterImpl;
import com.aptatek.pkuapp.device.notifications.IncubationCountdownNotificationFactory;
import com.aptatek.pkuapp.device.notifications.SampleWettingCountdownNotificationFactory;
import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.domain.interactor.countdown.CountdownTimeFormatter;
import com.aptatek.pkuapp.domain.notifications.CountdownNotificationFactory;
import com.aptatek.pkuapp.injection.qualifier.ApplicationContext;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class TestModule {

    @Named("incubation")
    @Provides
    CountdownNotificationFactory provideIncubationNotificationFactory(@ApplicationContext final Context context,
                                                                      final ResourceInteractor resourceInteractor,
                                                                      final NotificationManager notificationManager) {
        return new IncubationCountdownNotificationFactory(context, resourceInteractor, notificationManager);
    }

    @Named("samplewetting")
    @Provides
    CountdownNotificationFactory provideSampleWettingNotificationFactory(@ApplicationContext final Context context,
                                                                      final ResourceInteractor resourceInteractor,
                                                                      final NotificationManager notificationManager) {
        return new SampleWettingCountdownNotificationFactory(context, resourceInteractor, notificationManager);
    }

    @Provides
    CountdownTimeFormatter provideCountdownTimeFormatter(@NonNull final ResourceInteractor resourceInteractor) {
        return new CountdownTimeFormatterImpl(resourceInteractor);
    }

}
