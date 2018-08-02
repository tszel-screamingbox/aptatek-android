package com.aptatek.aptatek.injection.component.rangeinfo;

import com.aptatek.aptatek.injection.module.FragmentModule;
import com.aptatek.aptatek.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.aptatek.view.weekly.chart.WeeklyChartFragment;

import dagger.Subcomponent;

@Subcomponent(modules = {FragmentModule.class, RangeInfoModule.class})
public interface RangeInfoFragmentComponent {

    void inject(WeeklyChartFragment fragment);

}
