package com.aptatek.pkuapp.domain.interactor.parentalgate;

import android.support.annotation.NonNull;

public interface BirthDateFormatter {

    @NonNull
    String formatBirthDate(long timestamp);

}
