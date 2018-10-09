package com.aptatek.pkulab.injection.component;

import com.aptatek.pkulab.domain.interactor.PkuRangeInteractorTest;
import com.aptatek.pkulab.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkulab.view.rangeinfo.RangeInfoTest;
import com.aptatek.pkulab.view.rangesettings.RangeSettingsTest;

import dagger.Subcomponent;

@Subcomponent(modules = RangeInfoModule.class)
public interface RangeInfoComponent {

    void inject(PkuRangeInteractorTest test);

    void inject(RangeInfoTest test);

    void inject(RangeSettingsTest test);

}
