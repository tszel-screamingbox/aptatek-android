package com.aptatek.pkuapp.injection.component;

import android.content.Context;

import com.aptatek.pkuapp.injection.component.chart.ChartActivityComponent;
import com.aptatek.pkuapp.injection.component.rangeinfo.RangeInfoActivityComponent;
import com.aptatek.pkuapp.injection.component.test.TestActivityComponent;
import com.aptatek.pkuapp.injection.module.ActivityModule;
import com.aptatek.pkuapp.injection.module.chart.ChartModule;
import com.aptatek.pkuapp.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkuapp.injection.module.test.TestModule;
import com.aptatek.pkuapp.injection.qualifier.ActivityContext;
import com.aptatek.pkuapp.injection.scope.ActivityScope;
import com.aptatek.pkuapp.view.fingerprint.FingerprintActivity;
import com.aptatek.pkuapp.view.parentalgate.ParentalGateActivity;
import com.aptatek.pkuapp.view.pin.auth.AuthPinHostActivity;
import com.aptatek.pkuapp.view.pin.set.SetPinHostActivity;
import com.aptatek.pkuapp.view.settings.basic.SettingsActivity;
import com.aptatek.pkuapp.view.settings.reminder.ReminderSettingsActivity;
import com.aptatek.pkuapp.view.splash.SplashActivity;
import com.aptatek.pkuapp.view.toggle.ToggleActivity;

import dagger.Component;


@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(AuthPinHostActivity activity);

    void inject(FingerprintActivity activity);

    void inject(SetPinHostActivity activity);

    void inject(SplashActivity activity);

    void inject(ToggleActivity activity);

    void inject(ParentalGateActivity activity);

    void inject(ReminderSettingsActivity activity);

    void inject(SettingsActivity activity);

    // Activities injections should be come here
    @ActivityContext
    Context context();

    TestActivityComponent plus(TestModule module);

    RangeInfoActivityComponent plus(RangeInfoModule module);

    ChartActivityComponent plus(RangeInfoModule rangeInfoModule, ChartModule chartModule);

}
