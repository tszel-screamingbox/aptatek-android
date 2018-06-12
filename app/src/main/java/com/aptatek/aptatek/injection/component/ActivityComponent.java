package com.aptatek.aptatek.injection.component;

import android.content.Context;

import com.aptatek.aptatek.injection.component.test.TestActivityComponent;
import com.aptatek.aptatek.injection.module.ActivityModule;
import com.aptatek.aptatek.injection.module.test.TestModule;
import com.aptatek.aptatek.injection.qualifier.ActivityContext;
import com.aptatek.aptatek.injection.scope.ActivityScope;
import com.aptatek.aptatek.view.fingerprint.FingerprintActivity;
import com.aptatek.aptatek.view.main.MainActivity;
import com.aptatek.aptatek.view.pin.request.RequestPinHostActivity;
import com.aptatek.aptatek.view.pin.set.SetPinHostActivity;
import com.aptatek.aptatek.view.splash.SplashActivity;
import com.aptatek.aptatek.view.toggle.ToggleActivity;

import dagger.Component;


@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(FingerprintActivity activity);

    void inject(MainActivity activity);

    void inject(RequestPinHostActivity activity);

    void inject(SetPinHostActivity activity);

    void inject(SplashActivity activity);

    void inject(ToggleActivity activity);

    // Activities injections should be come here
    @ActivityContext
    Context context();

    TestActivityComponent plus(TestModule module);
}
