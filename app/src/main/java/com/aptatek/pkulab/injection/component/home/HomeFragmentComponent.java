package com.aptatek.pkulab.injection.component.home;

import com.aptatek.pkulab.injection.module.FragmentModule;
import com.aptatek.pkulab.injection.module.chart.ChartModule;
import com.aptatek.pkulab.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkulab.injection.module.test.TestModule;
import com.aptatek.pkulab.view.main.home.HomeFragment;

import dagger.Subcomponent;

@Subcomponent(modules = {FragmentModule.class, TestModule.class, RangeInfoModule.class, ChartModule.class})
public interface HomeFragmentComponent {

    void inject(HomeFragment fragment);

}

