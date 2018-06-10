package com.aptatek.aptatek.injection.component;

import com.aptatek.aptatek.injection.module.FragmentModule;
import com.aptatek.aptatek.injection.scope.FragmentScope;
import com.aptatek.aptatek.view.test.takesample.TakeSampleFragment;

import dagger.Component;


@FragmentScope
@Component(dependencies = ApplicationComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
    // Inject fragments, for example: void inject (Fragment fragment)

    void inject(TakeSampleFragment fragment);

}
