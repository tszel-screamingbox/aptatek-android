package com.aptatek.pkuapp.injection.component.parentalgate;

import com.aptatek.pkuapp.injection.module.FragmentModule;
import com.aptatek.pkuapp.injection.module.parentalgate.ParentalGateModule;
import com.aptatek.pkuapp.view.parentalgate.welcome.ParentalGateWelcomeFragment;

import dagger.Subcomponent;

@Subcomponent(modules = {FragmentModule.class, ParentalGateModule.class})
public interface ParentalGateFragmentComponent {

    void inject(ParentalGateWelcomeFragment activity);

}
