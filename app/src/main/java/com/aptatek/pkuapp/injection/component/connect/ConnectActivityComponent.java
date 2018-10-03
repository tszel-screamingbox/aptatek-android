package com.aptatek.pkuapp.injection.component.connect;

import com.aptatek.pkuapp.injection.module.ActivityModule;
import com.aptatek.pkuapp.injection.module.connect.ConnectModule;
import com.aptatek.pkuapp.view.connect.ConnectReaderActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {ActivityModule.class, ConnectModule.class})
public interface ConnectActivityComponent {

    void inject(ConnectReaderActivity activity);

}
