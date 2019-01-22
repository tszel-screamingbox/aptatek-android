package com.aptatek.pkulab.injection.module;

import android.app.NotificationManager;
import android.content.Context;

import com.aptatek.pkulab.device.notifications.BluetoothNotificationFactory;
import com.aptatek.pkulab.device.notifications.BluetoothNotificationFactoryImpl;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.injection.qualifier.ApplicationContext;

import dagger.Module;
import dagger.Provides;

@Module
public class BluetoothServiceModule {

    @Provides
    public BluetoothNotificationFactory provideNotificationFactory(@ApplicationContext final Context context,
                                                                   final ResourceInteractor resourceInteractor,
                                                                   final NotificationManager notificationManager) {
        return new BluetoothNotificationFactoryImpl(context, resourceInteractor, notificationManager);
    }

}
