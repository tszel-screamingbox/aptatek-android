package com.aptatek.aptatek.injection.component.test;

import com.aptatek.aptatek.injection.component.ApplicationComponent;
import com.aptatek.aptatek.injection.module.FragmentModule;
import com.aptatek.aptatek.injection.module.test.IncubationModule;
import com.aptatek.aptatek.injection.scope.FragmentScope;
import com.aptatek.aptatek.view.test.incubation.IncubationFragment;
import com.aptatek.aptatek.view.test.takesample.TakeSampleFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = ApplicationComponent.class, modules = {FragmentModule.class, IncubationModule.class})
public interface TestFragmentComponent {

    void inject(TakeSampleFragment fragment);

    void inject(IncubationFragment fragment);

}
