package com.aptatek.aptatek.injection.component;

import com.aptatek.aptatek.domain.IncubationInteractorTest;
import com.aptatek.aptatek.injection.module.test.TestModule;

import dagger.Subcomponent;

@Subcomponent(modules = TestModule.class)
public interface TestIncubationComponent {

    void inject(IncubationInteractorTest test);

}
