package com.aptatek.pkuapp.injection.module;

import org.fluttercode.datafactory.impl.DataFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class DataFactoryModule {

    @Provides
    public DataFactory provideDataFactory() {
        return new DataFactory();
    }

}
