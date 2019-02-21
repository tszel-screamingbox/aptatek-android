package com.aptatek.pkulab.injection.component;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;

import com.aptatek.pkulab.data.AptatekDatabase;
import com.aptatek.pkulab.device.DeviceHelper;
import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.domain.base.Mapper;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.testresult.TestResultDataSource;
import com.aptatek.pkulab.domain.interactor.pkurange.PkuRangeDataSource;
import com.aptatek.pkulab.domain.interactor.wetting.WettingDataSource;
import com.aptatek.pkulab.domain.manager.reader.BluetoothAdapter;
import com.aptatek.pkulab.domain.manager.reader.BluetoothConditionChecker;
import com.aptatek.pkulab.domain.manager.reader.BluetoothScanner;
import com.aptatek.pkulab.domain.manager.reader.ReaderManager;
import com.aptatek.pkulab.injection.qualifier.ApplicationContext;
import com.aptatek.pkulab.util.animation.AnimationHelper;

import java.io.File;
import java.util.Map;

import javax.inject.Named;

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

    TestResultDataSource provideCubeDataSource();

    WettingDataSource provideWettingDataSource();

    PkuRangeDataSource providePkuRangeDataSource();

    BluetoothAdapter provideBluetoothAdapter();

    BluetoothScanner provideBluetoothScanner();

    BluetoothConditionChecker provideBluetoothConditionChecker();

    ReaderManager provideReaderManager();

    @Named("databaseFile")
    File provideDbFile();
}
