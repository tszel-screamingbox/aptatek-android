package com.aptatek.pkulab.domain.interactor.wetting;


import androidx.annotation.NonNull;

public class WettingError extends Throwable {

    public WettingError(@NonNull final Throwable cause) {
        super(cause);
    }

}
