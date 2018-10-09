package com.aptatek.pkulab.injection.component;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;

import com.aptatek.pkulab.data.AptatekDatabase;
import com.aptatek.pkulab.device.DeviceHelper;
import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.domain.base.Mapper;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.cube.CubeDataSource;
import com.aptatek.pkulab.domain.interactor.pkurange.PkuRangeDataSource;
import com.aptatek.pkulab.domain.interactor.wetting.WettingDataSource;
import com.aptatek.pkulab.domain.manager.reader.ReaderManager;
import com.aptatek.pkulab.injection.qualifier.ApplicationContext;
import com.aptatek.pkulab.util.animation.AnimationHelper;

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
