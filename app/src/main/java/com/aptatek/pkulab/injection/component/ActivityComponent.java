package com.aptatek.pkulab.injection.component;

import android.content.Context;

import com.aptatek.pkulab.injection.component.chart.ChartActivityComponent;
import com.aptatek.pkulab.injection.component.scan.ScanActivityComponent;
import com.aptatek.pkulab.injection.component.main.MainActivityComponent;
import com.aptatek.pkulab.injection.component.rangeinfo.RangeInfoActivityComponent;
import com.aptatek.pkulab.injection.component.test.TestActivityComponent;
import com.aptatek.pkulab.injection.module.ActivityModule;
import com.aptatek.pkulab.injection.module.chart.ChartModule;
import com.aptatek.pkulab.injection.module.scan.ScanModule;
import com.aptatek.pkulab.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkulab.injection.module.test.TestModule;
import com.aptatek.pkulab.injection.qualifier.ActivityContext;
import com.aptatek.pkulab.injection.scope.ActivityScope;
import com.aptatek.pkulab.view.fingerprint.FingerprintActivity;
import com.aptatek.pkulab.view.parentalgate.ParentalGateActivity;
import com.aptatek.pkulab.view.pin.auth.AuthPinHostActivity;
import com.aptatek.pkulab.view.pin.set.SetPinHostActivity;
import com.aptatek.pkulab.view.settings.basic.SettingsActivity;
import com.aptatek.pkulab.view.settings.reminder.ReminderSettingsActivity;
import com.aptatek.pkulab.view.splash.SplashActivity;
import com.aptatek.pkulab.view.toggle.ToggleActivity;

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

    MainActivityComponent plus(TestModule testModule, RangeInfoModule rangeInfoModule, ChartModule chartModule);

    ScanActivityComponent plus(ScanModule scanModule);
}
