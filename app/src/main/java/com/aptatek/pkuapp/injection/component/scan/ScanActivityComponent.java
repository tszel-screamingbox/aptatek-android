package com.aptatek.pkuapp.injection.component.scan;

import com.aptatek.pkuapp.injection.module.ActivityModule;
import com.aptatek.pkuapp.injection.module.scan.ScanModule;
import com.aptatek.pkuapp.view.connect.ConnectReaderActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {ActivityModule.class, ScanModule.class})
public interface ScanActivityComponent {

    void inject(ConnectReaderActivity activity);

}
