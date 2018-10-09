package com.aptatek.pkuapp.injection.component;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;

import com.aptatek.pkuapp.data.AptatekDatabase;
import com.aptatek.pkuapp.device.DeviceHelper;
import com.aptatek.pkuapp.device.PreferenceManager;
import com.aptatek.pkuapp.domain.base.Mapper;
import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.domain.interactor.cube.CubeDataSource;
import com.aptatek.pkuapp.domain.interactor.pkurange.PkuRangeDataSource;
import com.aptatek.pkuapp.domain.interactor.wetting.WettingDataSource;
import com.aptatek.pkuapp.domain.manager.reader.ReaderManager;
import com.aptatek.pkuapp.injection.qualifier.ApplicationContext;
import com.aptatek.pkuapp.util.animation.AnimationHelper;

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

    WettingDataSource provideWettingDataSource();

    PkuRangeDataSource providePkuRangeDataSource();

    ReaderManager provideReaderManager();

}
