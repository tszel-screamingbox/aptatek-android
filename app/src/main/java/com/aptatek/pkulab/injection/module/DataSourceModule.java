package com.aptatek.pkulab.injection.module;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.data.datasource.FakeCubeDataGenerator;
import com.aptatek.pkulab.data.datasource.FakeCubeDataSourceImpl;
import com.aptatek.pkulab.data.datasource.PkuRangeDataSourceImpl;
import com.aptatek.pkulab.data.datasource.WettingDataSourceImpl;
import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.domain.interactor.cube.CubeDataSource;
import com.aptatek.pkulab.domain.interactor.pkurange.PkuRangeDataSource;
import com.aptatek.pkulab.domain.interactor.wetting.WettingDataSource;

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
