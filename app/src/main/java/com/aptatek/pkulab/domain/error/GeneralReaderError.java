package com.aptatek.pkulab.domain.error;

import android.support.annotation.NonNull;

public class GeneralReaderError extends ReaderError {

    private final int errorCode;

    public GeneralReaderError(@NonNull String message, final int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

}
