package com.aptatek.aptatek.injection.module;

import android.support.annotation.NonNull;

import com.aptatek.aptatek.data.datasource.FakeCubeDataGenerator;
import com.aptatek.aptatek.data.datasource.FakeCubeDataSourceImpl;
import com.aptatek.aptatek.data.datasource.IncubationDataSourceImpl;
import com.aptatek.aptatek.data.datasource.PkuRangeDataSourceImpl;
import com.aptatek.aptatek.data.datasource.WettingDataSourceImpl;
import com.aptatek.aptatek.device.PreferenceManager;
import com.aptatek.aptatek.domain.interactor.cube.CubeDataSource;
import com.aptatek.aptatek.domain.interactor.incubation.IncubationDataSource;
import com.aptatek.aptatek.domain.interactor.pkurange.PkuRangeDataSource;
import com.aptatek.aptatek.domain.interactor.samplewetting.WettingDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataSourceModule {

    @Singleton
    @Provides
    public CubeDataSource provideCubeDataSource(final FakeCubeDataGenerator dataGenerator) {
        return new FakeCubeDataSourceImpl(dataGenerator);
    }

    @Singleton
    @Provides
    IncubationDataSource provideIncubationDataSource(@NonNull final PreferenceManager preferenceManager) {
        return new IncubationDataSourceImpl(preferenceManager);
    }

    @Singleton
    @Provides
    WettingDataSource provideWettingDataSource(@NonNull final PreferenceManager preferenceManager) {
        return new WettingDataSourceImpl(preferenceManager);
    }

    @Singleton
    @Provides
    PkuRangeDataSource providePkuRangeDataSource(@NonNull final PreferenceManager preferenceManager) {
        return new PkuRangeDataSourceImpl(preferenceManager);
    }

}
