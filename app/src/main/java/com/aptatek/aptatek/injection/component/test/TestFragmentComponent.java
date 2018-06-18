package com.aptatek.aptatek.injection.component.test;

import com.aptatek.aptatek.injection.module.FragmentModule;
import com.aptatek.aptatek.injection.module.test.TestModule;
import com.aptatek.aptatek.view.test.tutorial.attachcube.AttachCubeFragment;
import com.aptatek.aptatek.view.test.canceltest.CancelTestFragment;
import com.aptatek.aptatek.view.test.incubation.IncubationFragment;
import com.aptatek.aptatek.view.test.tutorial.insertcasette.InsertCasetteFragment;
import com.aptatek.aptatek.view.test.tutorial.insertsample.InsertSampleFragment;
import com.aptatek.aptatek.view.test.takesample.TakeSampleFragment;
import com.aptatek.aptatek.view.test.samplewetting.SampleWettingFragment;

import dagger.Subcomponent;

@Subcomponent(modules = {FragmentModule.class, TestModule.class})
public interface TestFragmentComponent {

    void inject(TakeSampleFragment fragment);

    void inject(IncubationFragment fragment);

    void inject(CancelTestFragment fragment);

    void inject(InsertCasetteFragment fragment);

    void inject(AttachCubeFragment fragment);

    void inject(InsertSampleFragment fragment);

    void inject(SampleWettingFragment fragment);

}
