package com.aptatek.pkuapp.domain.error;

import android.support.annotation.NonNull;

public class ReaderError extends Throwable {

    private final int errorCode;

    public ReaderError(@NonNull String message, final int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
