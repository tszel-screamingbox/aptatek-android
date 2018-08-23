package com.aptatek.pkuapp.injection.component.main;

import com.aptatek.pkuapp.injection.module.ActivityModule;
import com.aptatek.pkuapp.injection.module.chart.ChartModule;
import com.aptatek.pkuapp.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkuapp.injection.module.test.TestModule;
import com.aptatek.pkuapp.view.main.MainActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {ActivityModule.class, TestModule.class, RangeInfoModule.class, ChartModule.class})
public interface MainActivityComponent {

    void inject(MainActivity activity);

}

