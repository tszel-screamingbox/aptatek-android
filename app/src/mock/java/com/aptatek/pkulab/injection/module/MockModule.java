package com.aptatek.pkulab.injection.module;

import org.fluttercode.datafactory.impl.DataFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class MockModule {

    @Provides
    public DataFactory provideDataFactory() {
        return new DataFactory();
    }

}
