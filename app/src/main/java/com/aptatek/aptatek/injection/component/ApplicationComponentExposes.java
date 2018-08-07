package com.aptatek.aptatek.injection.component;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;

import com.aptatek.aptatek.data.AptatekDatabase;
import com.aptatek.aptatek.device.DeviceHelper;
import com.aptatek.aptatek.device.PreferenceManager;
import com.aptatek.aptatek.domain.base.Mapper;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.interactor.cube.CubeDataSource;
import com.aptatek.aptatek.domain.interactor.incubation.IncubationDataSource;
import com.aptatek.aptatek.domain.interactor.pkurange.PkuRangeDataSource;
import com.aptatek.aptatek.domain.interactor.samplewetting.WettingDataSource;
import com.aptatek.aptatek.injection.qualifier.ApplicationContext;
import com.aptatek.aptatek.util.animation.AnimationHelper;

import java.util.Map;

public interface ApplicationComponentExposes {

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

    CubeDataSource provideCubeDataSource();

    IncubationDataSource provideIncubationDataSource();

    WettingDataSource provideWettingDataSource();

    PkuRangeDataSource providePkuRangeDataSource();

}
