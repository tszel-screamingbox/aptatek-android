package com.aptatek.pkulab.domain.interactor.auth;

import io.reactivex.annotations.NonNull;

public interface Callback {

    void onSucceeded();

    void onInvalidFingerprintDetected();

    void onErrorOccurred(@NonNull String message);
}
