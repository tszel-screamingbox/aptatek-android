package com.aptatek.aptatek.injection.component;

import com.aptatek.aptatek.AptatekApplication;
import com.aptatek.aptatek.injection.module.ApplicationModule;
import com.aptatek.aptatek.injection.module.DataMapperModule;
import com.aptatek.aptatek.injection.module.DataSourceModule;
import com.aptatek.aptatek.injection.module.DatabaseModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, DatabaseModule.class, DataMapperModule.class, DataSourceModule.class})
public interface ApplicationComponent extends ApplicationComponentExposes {

    // Application level injections should come here
    void inject(AptatekApplication application);

}
