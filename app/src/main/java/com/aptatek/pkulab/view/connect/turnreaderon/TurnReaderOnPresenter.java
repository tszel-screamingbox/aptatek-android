package com.aptatek.pkulab.view.connect.turnreaderon;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.view.connect.common.BaseConnectPresenter;
import com.aptatek.pkulab.view.connect.permission.PermissionResult;

import java.util.List;

public interface TurnReaderOnPresenter<V extends TurnReaderOnView> extends BaseConnectPresenter<V> {

    void onResumed();

    void onPaused();

    void connectTo(@NonNull ReaderDevice readerDevice);

    void evaluatePermissionResults(List<PermissionResult> results);

    void logScreenDisplayed();

    void disposeTest();

}
