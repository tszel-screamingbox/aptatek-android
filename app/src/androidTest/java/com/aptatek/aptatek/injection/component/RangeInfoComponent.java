package com.aptatek.aptatek.injection.component;

import com.aptatek.aptatek.domain.interactor.PkuRangeInteractorTest;
import com.aptatek.aptatek.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.aptatek.view.rangeinfo.RangeInfoTest;

import dagger.Subcomponent;

@Subcomponent(modules = RangeInfoModule.class)
public interface RangeInfoComponent {

    void inject(PkuRangeInteractorTest test);

    void inject(RangeInfoTest test);

}
