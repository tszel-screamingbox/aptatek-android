package com.aptatek.aptatek.injection.component;

import com.aptatek.aptatek.domain.interactor.IncubationInteractorTest;
import com.aptatek.aptatek.domain.interactor.SampleWettingInteractorTest;
import com.aptatek.aptatek.injection.module.test.TestModule;

import dagger.Subcomponent;

@Subcomponent(modules = TestModule.class)
public interface TestComponent {

    void inject(IncubationInteractorTest test);

    void inject(SampleWettingInteractorTest test);

}