package com.aptatek.aptatek.injection.module;

import android.support.annotation.NonNull;

import com.aptatek.aptatek.data.IncubationDataSourceImpl;
import com.aptatek.aptatek.device.PreferenceManager;
import com.aptatek.aptatek.domain.interactor.incubation.IncubationDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataModule {

    @Singleton
    @Provides
    IncubationDataSource provideIncubationDataSource(@NonNull final PreferenceManager preferenceManager) {
        return new IncubationDataSourceImpl(preferenceManager);
    }

}
