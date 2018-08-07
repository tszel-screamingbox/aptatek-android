package com.aptatek.pkuapp.injection.component;

import com.aptatek.pkuapp.domain.interactor.IncubationInteractorTest;
import com.aptatek.pkuapp.domain.interactor.SampleWettingInteractorTest;
import com.aptatek.pkuapp.injection.module.test.TestModule;

import dagger.Subcomponent;

@Subcomponent(modules = TestModule.class)
public interface TestComponent {

    void inject(IncubationInteractorTest test);

    void inject(SampleWettingInteractorTest test);

}