package com.aptatek.pkulab.injection.component.bluetooth;

import com.aptatek.pkulab.injection.component.ApplicationComponent;
import com.aptatek.pkulab.injection.module.BluetoothServiceModule;
import com.aptatek.pkulab.injection.module.ServiceModule;
import com.aptatek.pkulab.injection.scope.ServiceScope;
import com.aptatek.pkulab.view.service.BluetoothService;
import com.aptatek.pkulab.view.service.ExplicitBluetoothService;

import dagger.Component;

@ServiceScope
@Component(dependencies = ApplicationComponent.class, modules = {ServiceModule.class, BluetoothServiceModule.class})
public interface BluetoothComponent {

    void inject(BluetoothService service);

    void inject(ExplicitBluetoothService service);

}
