package com.aptatek.aptatek.domain.interactor.auth;

public interface Callback {

    void succeed();

    void invalidFingerprint();

    void errorOccurred(String message);
}