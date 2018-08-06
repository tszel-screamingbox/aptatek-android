package com.aptatek.aptatek.injection.component;

import com.aptatek.aptatek.injection.module.ApplicationModule;
import com.aptatek.aptatek.injection.module.DataFactoryModule;
import com.aptatek.aptatek.injection.module.DataMapperModule;
import com.aptatek.aptatek.injection.module.DataSourceModule;
import com.aptatek.aptatek.injection.module.DatabaseModule;
import com.aptatek.aptatek.injection.module.ReminderModule;
import com.aptatek.aptatek.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.aptatek.injection.module.test.TestModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, DataFactoryModule.class, DatabaseModule.class, DataMapperModule.class, DataSourceModule.class})
public interface AndroidTestComponent extends ApplicationComponent {

    TestComponent plus(TestModule module);

    RangeInfoComponent plus(RangeInfoModule module);

    ReminderTestComponent plus(ReminderModule reminderModule);

}
