package com.aptatek.aptatek.injection.component.test;

import com.aptatek.aptatek.injection.module.FragmentModule;
import com.aptatek.aptatek.injection.module.test.TestModule;
import com.aptatek.aptatek.view.test.canceltest.CancelTestFragment;
import com.aptatek.aptatek.view.test.incubation.IncubationFragment;
import com.aptatek.aptatek.view.test.takesample.TakeSampleFragment;

import dagger.Subcomponent;

@Subcomponent(modules = {FragmentModule.class, TestModule.class})
public interface TestFragmentComponent {

    void inject(TakeSampleFragment fragment);

    void inject(IncubationFragment fragment);

    void inject(CancelTestFragment fragment);

}