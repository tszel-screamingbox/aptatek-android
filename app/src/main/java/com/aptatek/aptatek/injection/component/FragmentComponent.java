package com.aptatek.aptatek.injection.component;

import com.aptatek.aptatek.injection.module.FragmentModule;
import com.aptatek.aptatek.injection.scope.FragmentScope;

import dagger.Component;


@FragmentScope
@Component(dependencies = ApplicationComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
    // Inject fragments, for example: void inject (Fragment fragment)

    void inject(AddPinFragment addPinFragment);

    void inject(ConfirmPinFragment confirmPinFragment);

    void inject(RequestPinFragment requestPinFragment);

    void inject(TakeSampleFragment fragment);

}
