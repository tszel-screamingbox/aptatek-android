package com.aptatek.pkuapp.injection.component.connect;

import com.aptatek.pkuapp.injection.module.FragmentModule;
import com.aptatek.pkuapp.injection.module.connect.ConnectModule;
import com.aptatek.pkuapp.view.connect.scan.ScanFragment;

import dagger.Subcomponent;

@Subcomponent(modules = {FragmentModule.class, ConnectModule.class})
public interface ConnectFragmentComponent {

    void inject(ScanFragment scanFragment);

}
