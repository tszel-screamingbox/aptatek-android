package com.aptatek.aptatek.injection.component.chart;

import com.aptatek.aptatek.injection.module.ActivityModule;
import com.aptatek.aptatek.injection.module.chart.ChartModule;
import com.aptatek.aptatek.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.aptatek.view.main.MainActivity;
import com.aptatek.aptatek.view.weekly.WeeklyResultActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {ActivityModule.class, RangeInfoModule.class, ChartModule.class})
public interface ChartActivityComponent {

    void inject(MainActivity mainActivity);

    void inject(WeeklyResultActivity activity);

}
