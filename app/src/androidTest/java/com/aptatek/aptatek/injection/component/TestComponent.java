package com.aptatek.aptatek.injection.component;

import com.aptatek.aptatek.domain.interactor.IncubationInteractorTest;
import com.aptatek.aptatek.domain.interactor.samplewetting.SampleWettingInteractor;
import com.aptatek.aptatek.injection.module.ApplicationModule;
import com.aptatek.aptatek.injection.module.DataFactoryModule;
import com.aptatek.aptatek.injection.module.DatabaseModule;
import com.aptatek.aptatek.injection.module.test.TestModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, DataFactoryModule.class, DatabaseModule.class, TestModule.class})
public interface TestComponent extends ApplicationComponent {

    void inject(IncubationInteractorTest test);

    void inject(SampleWettingInteractor test);

}