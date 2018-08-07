package com.aptatek.pkuapp.injection.component.chart;

import com.aptatek.pkuapp.injection.module.FragmentModule;
import com.aptatek.pkuapp.injection.module.chart.ChartModule;
import com.aptatek.pkuapp.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkuapp.view.weekly.chart.WeeklyChartFragment;

import dagger.Subcomponent;

@Subcomponent(modules = {FragmentModule.class, RangeInfoModule.class, ChartModule.class})
public interface ChartFragmentComponent {

    void inject(WeeklyChartFragment fragment);

}
