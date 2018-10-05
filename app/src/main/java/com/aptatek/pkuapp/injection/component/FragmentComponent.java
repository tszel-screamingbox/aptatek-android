package com.aptatek.pkuapp.injection.component;

import com.aptatek.pkuapp.injection.component.chart.ChartFragmentComponent;
import com.aptatek.pkuapp.injection.component.connect.ConnectFragmentComponent;
import com.aptatek.pkuapp.injection.component.parentalgate.ParentalGateFragmentComponent;
import com.aptatek.pkuapp.injection.component.test.TestFragmentComponent;
import com.aptatek.pkuapp.injection.module.FragmentModule;
import com.aptatek.pkuapp.injection.module.chart.ChartModule;
import com.aptatek.pkuapp.injection.module.connect.ConnectModule;
import com.aptatek.pkuapp.injection.module.parentalgate.ParentalGateModule;
import com.aptatek.pkuapp.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkuapp.injection.module.test.TestModule;
import com.aptatek.pkuapp.injection.scope.FragmentScope;
import com.aptatek.pkuapp.view.connect.connected.ConnectedFragment;
import com.aptatek.pkuapp.view.connect.enablebluetooth.EnableBluetoothFragment;
import com.aptatek.pkuapp.view.connect.permission.PermissionRequiredFragment;
import com.aptatek.pkuapp.view.connect.turnon.TurnOnFragment;
import com.aptatek.pkuapp.view.parentalgate.verification.ParentalGateVerificationFragment;
import com.aptatek.pkuapp.view.pin.auth.add.AuthPinFragment;
import com.aptatek.pkuapp.view.pin.set.add.AddPinFragment;
import com.aptatek.pkuapp.view.pin.set.confirm.ConfirmPinFragment;

import dagger.Component;


@FragmentScope
@Component(dependencies = ApplicationComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
    // Inject fragments, for example: void inject(Fragment fragment)

    void inject(AddPinFragment addPinFragment);

    void inject(ConfirmPinFragment confirmPinFragment);

    void inject(AuthPinFragment authPinFragment);

    void inject(ParentalGateVerificationFragment parentalGateVerificationFragment);

    void inject(TurnOnFragment turnOnFragment);

    void inject(PermissionRequiredFragment permissionRequiredFragment);

    void inject(EnableBluetoothFragment enableBluetoothFragment);

    void inject(ConnectedFragment connectedFragment);

    TestFragmentComponent plus(TestModule module);

    ParentalGateFragmentComponent plus(ParentalGateModule module);

    ChartFragmentComponent plus(RangeInfoModule module, ChartModule chartModule);

    ConnectFragmentComponent plus(ConnectModule connectModule);

}
