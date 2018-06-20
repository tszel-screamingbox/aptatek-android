package com.aptatek.aptatek.injection.module.test;

import android.app.NotificationManager;
import android.content.Context;
import android.support.annotation.NonNull;

import com.aptatek.aptatek.data.IncubationDataSourceImpl;
import com.aptatek.aptatek.device.formatter.InclubationTimeFormatterImpl;
import com.aptatek.aptatek.device.IncubationNotificationFactoryImpl;
import com.aptatek.aptatek.device.PreferenceManager;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.interactor.incubation.IncubationDataSource;
import com.aptatek.aptatek.domain.interactor.incubation.IncubationNotificationFactory;
import com.aptatek.aptatek.domain.interactor.incubation.IncubationTimeFormatter;
import com.aptatek.aptatek.injection.qualifier.ApplicationContext;

import dagger.Module;
import dagger.Provides;

@Module
public class TestModule {

    @Provides
    IncubationNotificationFactory provideIncubationNotificationFactory(@ApplicationContext final Context context,
                                                                       final ResourceInteractor resourceInteractor,
                                                                       final NotificationManager notificationManager) {
        return new IncubationNotificationFactoryImpl(context, resourceInteractor, notificationManager);
    }

    @Provides
    IncubationTimeFormatter provideIncubationTimeFormatter(@NonNull final ResourceInteractor resourceInteractor) {
        return new InclubationTimeFormatterImpl(resourceInteractor);
    }

    @Provides
    IncubationDataSource provideIncubationDataSource(@NonNull final PreferenceManager preferenceManager) {
        return new IncubationDataSourceImpl(preferenceManager);
    }

}
