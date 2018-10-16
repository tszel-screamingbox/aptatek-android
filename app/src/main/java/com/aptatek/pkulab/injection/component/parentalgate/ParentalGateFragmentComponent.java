package com.aptatek.pkulab.injection.component.parentalgate;

import com.aptatek.pkulab.injection.module.FragmentModule;
import com.aptatek.pkulab.injection.module.parentalgate.ParentalGateModule;
import com.aptatek.pkulab.view.parentalgate.welcome.ParentalGateWelcomeFragment;

import dagger.Subcomponent;

@Subcomponent(modules = {FragmentModule.class, ParentalGateModule.class})
public interface ParentalGateFragmentComponent {

    void inject(ParentalGateWelcomeFragment activity);

}
