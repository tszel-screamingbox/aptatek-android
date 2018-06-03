package com.aptatek.aptatek.injection.component;

import android.content.Context;

import com.aptatek.aptatek.injection.module.ActivityModule;
import com.aptatek.aptatek.injection.qualifier.ActivityContext;
import com.aptatek.aptatek.injection.scope.ActivityScope;
import com.aptatek.aptatek.view.main.MainActivity;
import com.aptatek.aptatek.view.splash.SplashActivity;
import com.aptatek.aptatek.view.test.TestActivity;
import com.aptatek.aptatek.view.toggle.ToggleActivity;

import dagger.Component;


@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activity);

    void inject(SplashActivity activity);

    void inject(ToggleActivity activity);

    void inject(TestActivity activity);

    // Activities injections should be come here
    @ActivityContext
    Context context();
}
