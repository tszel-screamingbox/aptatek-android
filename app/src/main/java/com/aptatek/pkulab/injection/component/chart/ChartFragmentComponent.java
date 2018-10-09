package com.aptatek.pkulab.injection.component.chart;

import com.aptatek.pkulab.injection.module.FragmentModule;
import com.aptatek.pkulab.injection.module.chart.ChartModule;
import com.aptatek.pkulab.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkulab.view.weekly.chart.WeeklyChartFragment;

import dagger.Subcomponent;

@Subcomponent(modules = {FragmentModule.class, RangeInfoModule.class, ChartModule.class})
public interface ChartFragmentComponent {

    void inject(WeeklyChartFragment fragment);

}
