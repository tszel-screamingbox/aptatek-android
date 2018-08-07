package com.aptatek.pkuapp.injection.module;

import android.app.Service;
import android.content.Context;
import android.support.annotation.NonNull;

import com.aptatek.pkuapp.injection.qualifier.ServiceContext;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {

    private final Service service;

    public ServiceModule(@NonNull final Service service) {
        this.service = service;
    }

    @ServiceContext
    @Provides
    public Context provideServiceContext() {
        return service;
    }
}
