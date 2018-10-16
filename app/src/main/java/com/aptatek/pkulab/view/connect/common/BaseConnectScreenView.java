package com.aptatek.pkulab.view.connect.common;

import android.support.annotation.NonNull;

import java.util.List;

public interface BaseConnectScreenView extends ConnectCommonView {

    void requestMissingPermissions(@NonNull List<String> permissions);

    void onActivityResumed();

}
