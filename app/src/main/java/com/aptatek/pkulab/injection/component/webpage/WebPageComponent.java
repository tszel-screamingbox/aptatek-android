package com.aptatek.pkulab.injection.component.webpage;

import com.aptatek.pkulab.injection.module.ActivityModule;
import com.aptatek.pkulab.injection.module.chart.ChartModule;
import com.aptatek.pkulab.view.settings.web.WebPageActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {ActivityModule.class, ChartModule.class})
public interface WebPageComponent {

    void inject(WebPageActivity webPageActivity);

}