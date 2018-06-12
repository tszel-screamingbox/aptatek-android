package com.aptatek.aptatek.injection.component;

import com.aptatek.aptatek.injection.component.test.TestFragmentComponent;
import com.aptatek.aptatek.injection.module.FragmentModule;
import com.aptatek.aptatek.injection.module.test.TestModule;
import com.aptatek.aptatek.injection.scope.FragmentScope;
import com.aptatek.aptatek.view.pin.request.add.RequestPinFragment;
import com.aptatek.aptatek.view.pin.set.add.AddPinFragment;
import com.aptatek.aptatek.view.pin.set.confirm.ConfirmPinFragment;
import com.aptatek.aptatek.view.test.takesample.TakeSampleFragment;

import dagger.Component;


@FragmentScope
@Component(dependencies = ApplicationComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
    // Inject fragments, for example: void inject (Fragment fragment

    void inject(AddPinFragment addPinFragment);

    void inject(ConfirmPinFragment confirmPinFragment);

    void inject(RequestPinFragment requestPinFragment);

    TestFragmentComponent plus(TestModule module);

}
