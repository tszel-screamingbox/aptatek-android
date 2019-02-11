package com.aptatek.pkulab.view.connect.turnreaderon;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.List;

public interface TurnReaderOnView extends MvpView {

    void showDeviceNotSupportedDialog();    // no ble feature
    void displayMissingPermissions();       // need to request permissions
    void displayReaderSelector(@NonNull List<ReaderDevice> readerDevices);  // display selector
    void displaySelfCheckAnimation();       // reader connected, should display self-checking animation
    void onSelfCheckComplete();             // everything's ok, we can proceed
    void displayNoReaderAvailable();        // after 5 sec of scan, when no readers were found

}
