package com.aptatek.aptatek.device;

import android.support.annotation.NonNull;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.interactor.countdown.CountdownTimeFormatter;

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
