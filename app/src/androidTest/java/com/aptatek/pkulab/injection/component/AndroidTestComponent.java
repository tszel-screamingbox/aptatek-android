package com.aptatek.pkulab.injection.component;

import com.aptatek.pkulab.injection.module.ApplicationModule;
import com.aptatek.pkulab.injection.module.DataFactoryModule;
import com.aptatek.pkulab.injection.module.DataMapperModule;
import com.aptatek.pkulab.injection.module.DataSourceModule;
import com.aptatek.pkulab.injection.module.DatabaseModule;
import com.aptatek.pkulab.injection.module.DeviceModule;
import com.aptatek.pkulab.injection.module.ReminderModule;
import com.aptatek.pkulab.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkulab.injection.module.test.TestModule;
import com.aptatek.pkulab.view.pin.SetPinScreenTest;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, DataFactoryModule.class, DatabaseModule.class, DataMapperModule.class, DataSourceModule.class, DeviceModule.class})
public interface AndroidTestComponent extends ApplicationComponent {

    TestComponent plus(TestModule module);

    RangeInfoComponent plus(RangeInfoModule module);

    ReminderTestComponent plus(ReminderModule reminderModule);

    void inject(SetPinScreenTest pinScreenTest);

}
