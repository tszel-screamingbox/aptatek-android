package com.aptatek.pkulab.injection.component.scan;

import com.aptatek.pkulab.injection.module.FragmentModule;
import com.aptatek.pkulab.injection.module.scan.ScanModule;
import com.aptatek.pkulab.view.connect.scan.ScanFragment;

import dagger.Subcomponent;

@Subcomponent(modules = {FragmentModule.class, ScanModule.class})
public interface ScanFragmentComponent {

    void inject(ScanFragment scanFragment);

}
