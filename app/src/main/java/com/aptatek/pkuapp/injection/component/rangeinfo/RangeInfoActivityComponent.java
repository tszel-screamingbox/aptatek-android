package com.aptatek.pkuapp.injection.component.rangeinfo;

import com.aptatek.pkuapp.injection.module.ActivityModule;
import com.aptatek.pkuapp.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkuapp.view.rangeinfo.RangeInfoActivity;
import com.aptatek.pkuapp.view.settings.pkulevel.RangeSettingsActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {ActivityModule.class, RangeInfoModule.class})
public interface RangeInfoActivityComponent {

    void inject(RangeInfoActivity activity);

    void inject(RangeSettingsActivity activity);

}
