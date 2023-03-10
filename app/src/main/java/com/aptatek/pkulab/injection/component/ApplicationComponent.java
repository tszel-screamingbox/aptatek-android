package com.aptatek.pkulab.injection.component;

import com.aptatek.pkulab.AptatekApplication;
import com.aptatek.pkulab.injection.module.AnalyticsModule;
import com.aptatek.pkulab.injection.module.ApplicationModule;
import com.aptatek.pkulab.injection.module.DataSourceModule;
import com.aptatek.pkulab.injection.module.DatabaseModule;
import com.aptatek.pkulab.injection.module.DeviceModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        DatabaseModule.class,
        DataSourceModule.class,
        DeviceModule.class,
        AnalyticsModule.class})
public interface ApplicationComponent extends ApplicationComponentExposes {

    // Application level injections should come here
    void inject(AptatekApplication application);
}
