package com.aptatek.pkuapp.injection.component.scan;

import com.aptatek.pkuapp.injection.module.FragmentModule;
import com.aptatek.pkuapp.injection.module.scan.ScanModule;
import com.aptatek.pkuapp.view.connect.scan.ScanFragment;

import dagger.Subcomponent;

@Subcomponent(modules = {FragmentModule.class, ScanModule.class})
public interface ScanFragmentComponent {

    void inject(ScanFragment scanFragment);

}
