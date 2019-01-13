package com.aptatek.pkulab.injection.component.test;

import com.aptatek.pkulab.injection.component.ApplicationComponent;
import com.aptatek.pkulab.injection.module.test.TestModule;
import com.aptatek.pkulab.injection.module.ServiceModule;
import com.aptatek.pkulab.injection.scope.ServiceScope;
import com.aptatek.pkulab.view.service.WettingForegroundService;

import dagger.Component;

@ServiceScope
@Component(dependencies = ApplicationComponent.class, modules = {ServiceModule.class, TestModule.class})
public interface TestServiceComponent {
    
    void inject(WettingForegroundService service);

}
