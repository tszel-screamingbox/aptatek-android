package com.aptatek.pkuapp.device.formatter;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.domain.interactor.parentalgate.BirthDateFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BirthDateFormatterImpl implements BirthDateFormatter {

    private final SimpleDateFormat dateFormat;

    public BirthDateFormatterImpl(@NonNull final ResourceInteractor resourceInteractor) {
        dateFormat = new SimpleDateFormat(resourceInteractor.getStringResource(R.string.parental_welcome_birthday_format), Locale.getDefault());
    }

    @NonNull
    @Override
    public String formatBirthDate(final long timestamp) {
        return dateFormat.format(new Date(timestamp));
    }

}
