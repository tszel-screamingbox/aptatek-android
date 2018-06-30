package com.aptatek.aptatek.injection.component.test;

import com.aptatek.aptatek.injection.component.ApplicationComponent;
import com.aptatek.aptatek.injection.module.test.TestModule;
import com.aptatek.aptatek.injection.module.ServiceModule;
import com.aptatek.aptatek.injection.scope.ServiceScope;
import com.aptatek.aptatek.view.test.incubation.IncubationReminderService;
import com.aptatek.aptatek.view.test.samplewetting.SampleWettingReminderService;

import dagger.Component;

@ServiceScope
@Component(dependencies = ApplicationComponent.class, modules = {ServiceModule.class, TestModule.class})
public interface TestServiceComponent {

    void inject(IncubationReminderService service);

    void inject(SampleWettingReminderService service);

}
