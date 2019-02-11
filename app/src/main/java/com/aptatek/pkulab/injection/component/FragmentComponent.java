package com.aptatek.pkulab.injection.component;

import com.aptatek.pkulab.injection.component.chart.ChartFragmentComponent;
import com.aptatek.pkulab.injection.component.home.HomeFragmentComponent;
import com.aptatek.pkulab.injection.component.parentalgate.ParentalGateFragmentComponent;
import com.aptatek.pkulab.injection.component.scan.BluetoothFragmentComponent;
import com.aptatek.pkulab.injection.component.test.TestFragmentComponent;
import com.aptatek.pkulab.injection.component.weekly.WeeklyFragmentComponent;
import com.aptatek.pkulab.injection.module.FragmentModule;
import com.aptatek.pkulab.injection.module.chart.ChartModule;
import com.aptatek.pkulab.injection.module.parentalgate.ParentalGateModule;
import com.aptatek.pkulab.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkulab.injection.module.scan.ScanModule;
import com.aptatek.pkulab.injection.module.test.TestModule;
import com.aptatek.pkulab.injection.scope.FragmentScope;
import com.aptatek.pkulab.view.connect.permission.PermissionRequiredFragment;
import com.aptatek.pkulab.view.parentalgate.verification.ParentalGateVerificationFragment;
import com.aptatek.pkulab.view.pin.auth.add.AuthPinFragment;
import com.aptatek.pkulab.view.pin.set.add.AddPinFragment;
import com.aptatek.pkulab.view.pin.set.confirm.ConfirmPinFragment;

import dagger.Component;


@FragmentScope
@Component(dependencies = ApplicationComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
    // Inject fragments, for example: void inject(Fragment fragment)

    void inject(AddPinFragment addPinFragment);

    void inject(ConfirmPinFragment confirmPinFragment);

    void inject(AuthPinFragment authPinFragment);

    void inject(ParentalGateVerificationFragment parentalGateVerificationFragment);

    void inject(PermissionRequiredFragment permissionRequiredFragment);

    HomeFragmentComponent plus(TestModule testModule, RangeInfoModule rangeInfoModule, ChartModule chartModule);

    WeeklyFragmentComponent add(RangeInfoModule rangeInfoModule, ChartModule chartModule);

    TestFragmentComponent plus(TestModule module);

    ParentalGateFragmentComponent plus(ParentalGateModule module);

    ChartFragmentComponent plus(RangeInfoModule module, ChartModule chartModule);

    BluetoothFragmentComponent plus(ScanModule scanModule);

}
