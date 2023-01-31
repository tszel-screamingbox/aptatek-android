package com.aptatek.pkulab.injection.module;

import android.app.Service;
import android.content.Context;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.injection.qualifier.ServiceContext;

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
