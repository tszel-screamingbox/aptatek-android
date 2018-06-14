package com.aptatek.aptatek.domain.interactor.incubation;

import android.support.annotation.NonNull;

public class IncubationError extends Throwable {

    public IncubationError(@NonNull final Throwable cause) {
        super(cause);
    }
}
