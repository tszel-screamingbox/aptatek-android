package com.aptatek.aptatek.device.formatter;

import android.support.annotation.NonNull;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.interactor.incubation.IncubationTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InclubationTimeFormatterImpl implements IncubationTimeFormatter {

    private final SimpleDateFormat dateFormat;

    public InclubationTimeFormatterImpl(@NonNull final ResourceInteractor resourceInteractor) {
        dateFormat = new SimpleDateFormat(resourceInteractor.getStringResource(R.string.test_incubation_countdown_format), Locale.getDefault());
    }

    @Override
    public String getFormattedRemaining(final long remaining) {
        return dateFormat.format(new Date(remaining));
    }
}
