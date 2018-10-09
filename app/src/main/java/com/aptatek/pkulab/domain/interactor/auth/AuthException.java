package com.aptatek.pkulab.domain.interactor.auth;

public class AuthException extends Exception {

    public AuthException(final String message) {
        super(message);
    }

    public AuthException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
