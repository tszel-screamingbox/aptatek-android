package com.aptatek.pkuapp.device.formatter;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.domain.interactor.countdown.CountdownTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CountdownTimeFormatterImpl implements CountdownTimeFormatter {

    private final SimpleDateFormat dateFormat;

    public CountdownTimeFormatterImpl(@NonNull final ResourceInteractor resourceInteractor) {
        dateFormat = new SimpleDateFormat(resourceInteractor.getStringResource(R.string.test_countdown_format), Locale.getDefault());
    }

    @Override
    public String getFormattedRemaining(final long remaining) {
        return dateFormat.format(new Date(remaining));
    }
}
