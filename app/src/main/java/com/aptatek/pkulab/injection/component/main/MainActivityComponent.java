package com.aptatek.pkulab.injection.component.main;

import com.aptatek.pkulab.injection.module.ActivityModule;
import com.aptatek.pkulab.injection.module.chart.ChartModule;
import com.aptatek.pkulab.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkulab.injection.module.test.TestModule;
import com.aptatek.pkulab.view.main.MainActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {ActivityModule.class, TestModule.class, RangeInfoModule.class, ChartModule.class})
public interface MainActivityComponent {

    void inject(MainActivity activity);

}

