package com.aptatek.aptatek.injection.component.test;

import com.aptatek.aptatek.injection.component.ApplicationComponent;
import com.aptatek.aptatek.injection.module.test.IncubationModule;
import com.aptatek.aptatek.injection.module.ServiceModule;
import com.aptatek.aptatek.injection.scope.ServiceScope;
import com.aptatek.aptatek.view.test.incubation.IncubationReminderService;

import dagger.Component;

@ServiceScope
@Component(dependencies = ApplicationComponent.class, modules = {ServiceModule.class, IncubationModule.class})
public interface TestServiceComponent {

    void inject(IncubationReminderService service);

}
