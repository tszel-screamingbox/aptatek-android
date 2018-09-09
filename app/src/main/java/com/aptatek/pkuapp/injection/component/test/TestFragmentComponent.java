package com.aptatek.pkuapp.injection.component.test;

import com.aptatek.pkuapp.injection.module.FragmentModule;
import com.aptatek.pkuapp.injection.module.test.TestModule;
import com.aptatek.pkuapp.view.test.canceltest.CancelTestFragment;
import com.aptatek.pkuapp.view.test.incubation.IncubationFragment;
import com.aptatek.pkuapp.view.test.samplewetting.SampleWettingFragment;
import com.aptatek.pkuapp.view.test.takesample.TakeSampleFragment;
import com.aptatek.pkuapp.view.test.tutorial.attachcube.AttachCubeFragment;
import com.aptatek.pkuapp.view.test.tutorial.insertcasette.InsertCasetteFragment;
import com.aptatek.pkuapp.view.test.tutorial.insertsample.InsertSampleFragment;

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
