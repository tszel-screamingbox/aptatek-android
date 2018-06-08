package com.aptatek.aptatek.injection.component;

import android.content.Context;

import com.aptatek.aptatek.AptatekApplication;
import com.aptatek.aptatek.device.DeviceHelper;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.interactor.incubation.IncubationDataSource;
import com.aptatek.aptatek.injection.module.ApplicationModule;
import com.aptatek.aptatek.injection.module.DataModule;
import com.aptatek.aptatek.injection.qualifier.ApplicationContext;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, DataModule.class})
public interface ApplicationComponent {

    // Application level injections should come here
    void inject(AptatekApplication application);

    ResourceInteractor resourceInteractor();

    DeviceHelper deviceHelper();

    IncubationDataSource incubationDataSource();

    @ApplicationContext
    Context context();
}
