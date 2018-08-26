package com.aptatek.pkuapp.injection.component.chart;

import com.aptatek.pkuapp.injection.module.ActivityModule;
import com.aptatek.pkuapp.injection.module.chart.ChartModule;
import com.aptatek.pkuapp.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkuapp.view.weekly.WeeklyResultActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {ActivityModule.class, RangeInfoModule.class, ChartModule.class})
public interface ChartActivityComponent {

    void inject(WeeklyResultActivity activity);

}
