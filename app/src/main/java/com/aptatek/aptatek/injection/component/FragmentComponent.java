package com.aptatek.aptatek.injection.component;

import com.aptatek.aptatek.injection.component.test.TestFragmentComponent;
import com.aptatek.aptatek.injection.module.FragmentModule;
import com.aptatek.aptatek.injection.module.test.TestModule;
import com.aptatek.aptatek.injection.scope.FragmentScope;
import com.aptatek.aptatek.view.parentalgate.verification.ParentalGateVerificationFragment;
import com.aptatek.aptatek.view.parentalgate.welcome.ParentalGateWelcomeFragment;
import com.aptatek.aptatek.view.pin.auth.add.AuthPinFragment;
import com.aptatek.aptatek.view.pin.set.add.AddPinFragment;
import com.aptatek.aptatek.view.pin.set.confirm.ConfirmPinFragment;

import dagger.Component;


@FragmentScope
@Component(dependencies = ApplicationComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
    // Inject fragments, for example: void inject (Fragment fragment

    void inject(AddPinFragment addPinFragment);

    void inject(ConfirmPinFragment confirmPinFragment);

    void inject(AuthPinFragment authPinFragment);

    void inject(ParentalGateWelcomeFragment parentalGateWelcomeFragment);

    void inject(ParentalGateVerificationFragment parentalGateVerificationFragment);

    TestFragmentComponent plus(TestModule module);

}
