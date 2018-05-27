package com.aptatek.aptatek.injection.component;

import com.aptatek.aptatek.injection.module.ApplicationModule;
import com.aptatek.aptatek.injection.module.DataFactoryModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, DataFactoryModule.class})
public interface TestComponent extends ApplicationComponent {

}
