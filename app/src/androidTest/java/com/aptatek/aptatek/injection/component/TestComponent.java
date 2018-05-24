package com.aptatek.aptatek.injection.component;

import com.aptatek.aptatek.injection.module.ApplicationModule;
import com.aptatek.aptatek.injection.module.DataFactoryModule;
import com.aptatek.aptatek.injection.module.DatabaseModule;
import com.aptatek.aptatek.interactor.ReadingInteractorTest;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, DatabaseModule.class, DataFactoryModule.class})
public interface TestComponent extends ApplicationComponent {

    void inject(ReadingInteractorTest readingInteractorTest);

}
