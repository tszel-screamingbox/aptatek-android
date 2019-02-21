package com.aptatek.pkulab.injection.component;

import com.aptatek.pkulab.AptatekApplication;
import com.aptatek.pkulab.injection.module.ApplicationModule;
import com.aptatek.pkulab.injection.module.DataMapperModule;
import com.aptatek.pkulab.injection.module.DataSourceModule;
import com.aptatek.pkulab.injection.module.DatabaseModule;
import com.aptatek.pkulab.injection.module.DeviceModule;
import com.aptatek.pkulab.injection.module.scan.ScanModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, DatabaseModule.class, DataMapperModule.class, DataSourceModule.class, DeviceModule.class, ScanModule.class})
public interface ApplicationComponent extends ApplicationComponentExposes {

    // Application level injections should come here
    void inject(AptatekApplication application);

}
