package com.aptatek.aptatek.injection.component.rangeinfo;

import com.aptatek.aptatek.injection.module.ActivityModule;
import com.aptatek.aptatek.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.aptatek.view.rangeinfo.RangeInfoActivity;
import com.aptatek.aptatek.view.settings.pkulevel.RangeSettingsActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {ActivityModule.class, RangeInfoModule.class})
public interface RangeInfoActivityComponent {

    void inject(RangeInfoActivity activity);

    void inject(RangeSettingsActivity activity);

}