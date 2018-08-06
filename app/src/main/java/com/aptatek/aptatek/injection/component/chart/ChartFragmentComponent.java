package com.aptatek.aptatek.injection.component.chart;

import com.aptatek.aptatek.injection.module.FragmentModule;
import com.aptatek.aptatek.injection.module.chart.ChartModule;
import com.aptatek.aptatek.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.aptatek.view.weekly.chart.WeeklyChartFragment;

import dagger.Subcomponent;

@Subcomponent(modules = {FragmentModule.class, RangeInfoModule.class, ChartModule.class})
public interface ChartFragmentComponent {

    void inject(WeeklyChartFragment fragment);

}
