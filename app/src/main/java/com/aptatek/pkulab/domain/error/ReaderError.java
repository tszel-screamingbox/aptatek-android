package com.aptatek.pkulab.domain.error;

public class ReaderError extends Throwable {

    public ReaderError() {
    }

    public ReaderError(String message) {
        super(message);
    }

    public ReaderError(String message, Throwable cause) {
        super(message, cause);
    }

    public ReaderError(Throwable cause) {
        super(cause);
    }

    public ReaderError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
