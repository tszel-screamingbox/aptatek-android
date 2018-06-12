package com.aptatek.aptatek.injection.component;

import com.aptatek.aptatek.injection.component.test.TestFragmentComponent;
import com.aptatek.aptatek.injection.module.FragmentModule;
import com.aptatek.aptatek.injection.module.test.TestModule;
import com.aptatek.aptatek.injection.scope.FragmentScope;

import dagger.Component;


@FragmentScope
@Component(dependencies = ApplicationComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
    // Inject fragments, for example: void inject (Fragment fragment)

    TestFragmentComponent plus(TestModule module);

}
