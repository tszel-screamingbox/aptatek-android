package com.aptatek.pkulab.domain.manager.reader;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.domain.error.TimeServerError;

public interface TimeServerCallbacks {

    void onOperationSuccessful();

    void onOperationFailed(@NonNull TimeServerError error);

}
