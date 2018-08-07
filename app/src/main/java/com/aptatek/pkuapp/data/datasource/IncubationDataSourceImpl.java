package com.aptatek.pkuapp.data.datasource;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.device.PreferenceManager;
import com.aptatek.pkuapp.domain.interactor.incubation.IncubationDataSource;
import com.aptatek.pkuapp.util.Constants;

public class IncubationDataSourceImpl implements IncubationDataSource {

    private final PreferenceManager preferenceManager;

    public IncubationDataSourceImpl(@NonNull final PreferenceManager preferenceManager) {
        this.preferenceManager = preferenceManager;
    }

    @Override
    public boolean hasRunningIncubation() {
        final long incubationStart = preferenceManager.getIncubationStart();
        return incubationStart > 0L
                && System.currentTimeMillis() - incubationStart <= Constants.DEFAULT_INCUBATION_PERIOD;
    }

    @Override
    public long getIncubationStart() {
        return preferenceManager.getIncubationStart();
    }

    @Override
    public void startIncubation() {
        preferenceManager.setIncubationStart(System.currentTimeMillis());
    }

    @Override
    public void stopIncubation() {
        preferenceManager.setIncubationStart(0L);
    }
}
