package com.aptatek.pkulab.injection.component.rangeinfo;

import com.aptatek.pkulab.injection.module.ActivityModule;
import com.aptatek.pkulab.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkulab.view.rangeinfo.RangeInfoActivity;
import com.aptatek.pkulab.view.settings.pkulevel.RangeSettingsActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {ActivityModule.class, RangeInfoModule.class})
public interface RangeInfoActivityComponent {

    void inject(RangeInfoActivity activity);

    void inject(RangeSettingsActivity activity);

}
