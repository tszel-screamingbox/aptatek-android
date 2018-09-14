package com.aptatek.pkuapp.injection.module;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.data.datasource.FakeCubeDataGenerator;
import com.aptatek.pkuapp.data.datasource.FakeCubeDataSourceImpl;
import com.aptatek.pkuapp.data.datasource.PkuRangeDataSourceImpl;
import com.aptatek.pkuapp.data.datasource.WettingDataSourceImpl;
import com.aptatek.pkuapp.device.PreferenceManager;
import com.aptatek.pkuapp.domain.interactor.cube.CubeDataSource;
import com.aptatek.pkuapp.domain.interactor.pkurange.PkuRangeDataSource;
import com.aptatek.pkuapp.domain.interactor.wetting.WettingDataSource;

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
    WettingDataSource provideWettingDataSource(@NonNull final PreferenceManager preferenceManager) {
        return new WettingDataSourceImpl(preferenceManager);
    }

    @Singleton
    @Provides
    PkuRangeDataSource providePkuRangeDataSource(@NonNull final PreferenceManager preferenceManager) {
        return new PkuRangeDataSourceImpl(preferenceManager);
    }

}
