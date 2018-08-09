package com.aptatek.pkuapp.injection.component.test;

import com.aptatek.pkuapp.injection.module.ActivityModule;
import com.aptatek.pkuapp.injection.module.test.TestModule;
import com.aptatek.pkuapp.view.test.TestActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {ActivityModule.class, TestModule.class})
public interface TestActivityComponent {

    void inject(TestActivity activity);

}