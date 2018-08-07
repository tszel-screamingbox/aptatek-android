package com.aptatek.pkuapp.domain.interactor.incubation;

import android.support.annotation.NonNull;

public class IncubationError extends Throwable {

    public IncubationError(@NonNull final Throwable cause) {
        super(cause);
    }
}
