package com.aptatek.pkulab.injection.component.test;

import com.aptatek.pkulab.injection.module.ActivityModule;
import com.aptatek.pkulab.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkulab.injection.module.test.TestModule;
import com.aptatek.pkulab.view.test.TestActivity;
import com.aptatek.pkulab.view.test.result.TestResultActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {ActivityModule.class, TestModule.class, RangeInfoModule.class})
public interface TestActivityComponent {

    void inject(TestActivity activity);

    void inject(TestResultActivity activity);

}
