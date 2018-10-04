package com.aptatek.pkuapp.injection.component;

import com.aptatek.pkuapp.AptatekApplication;
import com.aptatek.pkuapp.injection.module.ApplicationModule;
import com.aptatek.pkuapp.injection.module.DataMapperModule;
import com.aptatek.pkuapp.injection.module.DataSourceModule;
import com.aptatek.pkuapp.injection.module.DatabaseModule;
import com.aptatek.pkuapp.injection.module.DeviceModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, DatabaseModule.class, DataMapperModule.class, DataSourceModule.class, DeviceModule.class})
public interface ApplicationComponent extends ApplicationComponentExposes {

    // Application level injections should come here
    void inject(AptatekApplication application);

}
