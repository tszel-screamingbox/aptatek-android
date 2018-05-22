package com.aptatek.aptatek.injection.component;

import com.aptatek.aptatek.AptatekApplication;
import com.aptatek.aptatek.domain.ResourceInteractor;
import com.aptatek.aptatek.injection.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    // Application level injections should come here
    void inject(AptatekApplication application);

    ResourceInteractor resourceInteractor();
}
