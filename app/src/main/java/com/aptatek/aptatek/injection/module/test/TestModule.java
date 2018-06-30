package com.aptatek.aptatek.injection.module.test;

import android.app.NotificationManager;
import android.content.Context;
import android.support.annotation.NonNull;

import com.aptatek.aptatek.data.IncubationDataSourceImpl;
import com.aptatek.aptatek.data.WettingDataSourceImpl;
import com.aptatek.aptatek.device.notifications.IncubationCountdownNotificationFactory;
import com.aptatek.aptatek.device.PreferenceManager;
import com.aptatek.aptatek.device.formatter.CountdownTimeFormatterImpl;
import com.aptatek.aptatek.device.notifications.SampleWettingCountdownNotificationFactory;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.interactor.countdown.CountdownTimeFormatter;
import com.aptatek.aptatek.domain.interactor.incubation.IncubationDataSource;
import com.aptatek.aptatek.domain.notifications.CountdownNotificationFactory;
import com.aptatek.aptatek.domain.interactor.samplewetting.WettingDataSource;
import com.aptatek.aptatek.injection.qualifier.ApplicationContext;

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

    @Provides
    IncubationDataSource provideIncubationDataSource(@NonNull final PreferenceManager preferenceManager) {
        return new IncubationDataSourceImpl(preferenceManager);
    }

    @Provides
    WettingDataSource provideWettingDataSource(@NonNull final PreferenceManager preferenceManager) {
        return new WettingDataSourceImpl(preferenceManager);
    }

}
