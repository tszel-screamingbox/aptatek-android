package com.aptatek.aptatek.injection.component;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;

import com.aptatek.aptatek.AptatekApplication;
import com.aptatek.aptatek.data.AptatekDatabase;
import com.aptatek.aptatek.device.DeviceHelper;
import com.aptatek.aptatek.device.PreferenceManager;
import com.aptatek.aptatek.domain.base.Mapper;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.injection.module.ApplicationModule;
import com.aptatek.aptatek.injection.module.DataMapperModule;
import com.aptatek.aptatek.injection.module.DatabaseModule;
import com.aptatek.aptatek.injection.qualifier.ApplicationContext;
import com.aptatek.aptatek.util.animation.AnimationHelper;

import java.util.Map;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, DatabaseModule.class, DataMapperModule.class})
public interface ApplicationComponent {

    // Application level injections should come here
    void inject(AptatekApplication application);

    AnimationHelper animationHelper();

    ResourceInteractor resourceInteractor();

    DeviceHelper deviceHelper();

    PreferenceManager preferenceManager();

    NotificationManager notificationManager();

    NotificationManagerCompat notificationManagerCompat();

    AptatekDatabase aptatekDatabase();

    Map<Class<?>, Mapper<?, ?>> mapperProvider();

    @ApplicationContext
    Context context();
}
