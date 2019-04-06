package com.aptatek.pkulab.injection.component.webpage;

import com.aptatek.pkulab.injection.module.FragmentModule;
import com.aptatek.pkulab.injection.module.chart.ChartModule;
import com.aptatek.pkulab.view.settings.web.fragment.WebPageFragment;

import dagger.Subcomponent;

@Subcomponent(modules = {FragmentModule.class, ChartModule.class})
public interface WebPageComponent {

    void inject(WebPageFragment webPageFragment);

}