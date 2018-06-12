package com.aptatek.aptatek.injection.component.test;

import com.aptatek.aptatek.injection.module.ActivityModule;
import com.aptatek.aptatek.injection.module.test.TestModule;
import com.aptatek.aptatek.view.test.TestActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {ActivityModule.class, TestModule.class})
public interface TestActivityComponent {

    void inject(TestActivity activity);

}
