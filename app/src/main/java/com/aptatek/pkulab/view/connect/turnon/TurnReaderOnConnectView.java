package com.aptatek.pkulab.view.connect.turnon;

import com.aptatek.pkulab.view.connect.common.BaseConnectScreenView;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnView;

public interface TurnReaderOnConnectView extends BaseConnectScreenView, TurnReaderOnView {

    void showDeviceNotSupported();

}
