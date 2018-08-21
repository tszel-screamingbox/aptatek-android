package com.aptatek.pkuapp.data.datasource;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.device.PreferenceManager;
import com.aptatek.pkuapp.domain.interactor.incubation.IncubationDataSource;
import com.aptatek.pkuapp.domain.interactor.incubation.IncubationStatus;
import com.aptatek.pkuapp.util.Constants;

public class IncubationDataSourceImpl implements IncubationDataSource {

    private final PreferenceManager preferenceManager;

    public IncubationDataSourceImpl(@NonNull final PreferenceManager preferenceManager) {
        this.preferenceManager = preferenceManager;
    }

    @Override
    public IncubationStatus getIncubationStatus() {
        final long incubationStart = preferenceManager.getIncubationStart();

        if (incubationStart <= 0L) {
            return IncubationStatus.NOT_STARTED;
        } else if(System.currentTimeMillis() - incubationStart <= Constants.DEFAULT_INCUBATION_PERIOD) {
            return IncubationStatus.RUNNING;
        } else {
            return IncubationStatus.FINISHED;
        }
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
    public void resetIncubation() {
        preferenceManager.setIncubationStart(0L);
    }

    @Override
    public void skipIncubation() {
        preferenceManager.setIncubationStart(System.currentTimeMillis() - Constants.DEFAULT_INCUBATION_PERIOD);
    }
}
