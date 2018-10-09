package com.aptatek.pkuapp.injection.component;

import com.aptatek.pkuapp.injection.module.ApplicationModule;
import com.aptatek.pkuapp.injection.module.DataFactoryModule;
import com.aptatek.pkuapp.injection.module.DataMapperModule;
import com.aptatek.pkuapp.injection.module.DataSourceModule;
import com.aptatek.pkuapp.injection.module.DatabaseModule;
import com.aptatek.pkuapp.injection.module.DeviceModule;
import com.aptatek.pkuapp.injection.module.ReminderModule;
import com.aptatek.pkuapp.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkuapp.injection.module.test.TestModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, DataFactoryModule.class, DatabaseModule.class, DataMapperModule.class, DataSourceModule.class, DeviceModule.class})
public interface AndroidTestComponent extends ApplicationComponent {

    TestComponent plus(TestModule module);

    RangeInfoComponent plus(RangeInfoModule module);

    ReminderTestComponent plus(ReminderModule reminderModule);

}
