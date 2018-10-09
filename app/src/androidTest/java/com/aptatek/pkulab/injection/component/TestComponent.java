package com.aptatek.pkulab.injection.component;

import com.aptatek.pkulab.domain.interactor.WettingInteractorTest;
import com.aptatek.pkulab.injection.module.test.TestModule;

import dagger.Subcomponent;

@Subcomponent(modules = TestModule.class)
public interface TestComponent {

    void inject(WettingInteractorTest test);

}