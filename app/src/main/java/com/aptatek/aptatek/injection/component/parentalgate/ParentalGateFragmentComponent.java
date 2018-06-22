package com.aptatek.aptatek.injection.component.parentalgate;

import com.aptatek.aptatek.injection.module.FragmentModule;
import com.aptatek.aptatek.injection.module.parentalgate.ParentalGateModule;
import com.aptatek.aptatek.view.parentalgate.welcome.ParentalGateWelcomeFragment;

import dagger.Subcomponent;

@Subcomponent(modules = {FragmentModule.class, ParentalGateModule.class})
public interface ParentalGateFragmentComponent {

    void inject(ParentalGateWelcomeFragment activity);

}
