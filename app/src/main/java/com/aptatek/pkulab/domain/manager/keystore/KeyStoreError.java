package com.aptatek.pkulab.domain.manager.keystore;

import android.support.annotation.NonNull;

public class KeyStoreError extends Throwable {
    public KeyStoreError(@NonNull final Throwable cause) {
        super(cause);
    }
}
