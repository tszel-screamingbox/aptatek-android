package com.aptatek.pkulab.injection.component.weekly;

import com.aptatek.pkulab.injection.module.FragmentModule;
import com.aptatek.pkulab.injection.module.chart.ChartModule;
import com.aptatek.pkulab.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkulab.view.main.weekly.WeeklyResultFragment;

import dagger.Subcomponent;

@Subcomponent(modules = {FragmentModule.class, RangeInfoModule.class, ChartModule.class})
public interface WeeklyFragmentComponent {

    void inject(WeeklyResultFragment fragment);

}

