package com.aptatek.pkulab.injection.component.chart;

import com.aptatek.pkulab.injection.module.ActivityModule;
import com.aptatek.pkulab.injection.module.chart.ChartModule;
import com.aptatek.pkulab.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkulab.view.weekly.WeeklyResultActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {ActivityModule.class, RangeInfoModule.class, ChartModule.class})
public interface ChartActivityComponent {

    void inject(WeeklyResultActivity activity);

}
