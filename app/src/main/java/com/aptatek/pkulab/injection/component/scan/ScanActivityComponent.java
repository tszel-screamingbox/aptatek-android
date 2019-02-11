package com.aptatek.pkulab.injection.component.scan;

import com.aptatek.pkulab.injection.module.ActivityModule;
import com.aptatek.pkulab.injection.module.scan.ScanModule;
import com.aptatek.pkulab.view.connect.ConnectReaderActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {ActivityModule.class, ScanModule.class})
public interface ScanActivityComponent {

    void inject(ConnectReaderActivity activity);


}
