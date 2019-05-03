package com.aptatek.pkulab.injection.module;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.data.AptatekDatabase;
import com.aptatek.pkulab.data.datasource.PkuRangeDataSourceImpl;
import com.aptatek.pkulab.data.datasource.WettingDataSourceImpl;
import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.domain.interactor.pkurange.PkuRangeDataSource;
import com.aptatek.pkulab.domain.interactor.testresult.TestResultDataSource;
import com.aptatek.pkulab.domain.interactor.wetting.WettingDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataSourceModule {

    @Singleton
    @Provides
    public TestResultDataSource provideTestResultDataSource(final AptatekDatabase aptatekDatabase) {
        return aptatekDatabase.getTestResultDao();
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
