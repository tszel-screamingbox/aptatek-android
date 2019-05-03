package com.aptatek.pkulab.domain.interactor.parentalgate;


import androidx.annotation.NonNull;

public interface BirthDateFormatter {

    @NonNull
    String formatBirthDate(long timestamp);

}
