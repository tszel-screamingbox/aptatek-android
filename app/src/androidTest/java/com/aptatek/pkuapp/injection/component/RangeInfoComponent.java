package com.aptatek.pkuapp.injection.component;

import com.aptatek.pkuapp.domain.interactor.PkuRangeInteractorTest;
import com.aptatek.pkuapp.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkuapp.view.rangeinfo.RangeInfoTest;
import com.aptatek.pkuapp.view.rangesettings.RangeSettingsTest;

import dagger.Subcomponent;

@Subcomponent(modules = RangeInfoModule.class)
public interface RangeInfoComponent {

    void inject(PkuRangeInteractorTest test);

    void inject(RangeInfoTest test);

    void inject(RangeSettingsTest test);

}
