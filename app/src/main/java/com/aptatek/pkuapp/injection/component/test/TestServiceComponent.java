package com.aptatek.pkuapp.injection.component.test;

import com.aptatek.pkuapp.injection.component.ApplicationComponent;
import com.aptatek.pkuapp.injection.module.test.TestModule;
import com.aptatek.pkuapp.injection.module.ServiceModule;
import com.aptatek.pkuapp.injection.scope.ServiceScope;
import com.aptatek.pkuapp.view.test.wetting.WettingReminderService;

import dagger.Component;

@ServiceScope
@Component(dependencies = ApplicationComponent.class, modules = {ServiceModule.class, TestModule.class})
public interface TestServiceComponent {
    
    void inject(WettingReminderService service);

}
