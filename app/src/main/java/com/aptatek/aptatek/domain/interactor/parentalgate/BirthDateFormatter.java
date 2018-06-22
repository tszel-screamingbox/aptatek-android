package com.aptatek.aptatek.domain.interactor.parentalgate;

import android.support.annotation.NonNull;

public interface BirthDateFormatter {

    @NonNull
    String formatBirthDate(long timestamp);

}
