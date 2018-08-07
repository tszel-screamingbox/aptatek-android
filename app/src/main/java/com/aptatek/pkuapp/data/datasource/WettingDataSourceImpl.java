package com.aptatek.pkuapp.data.datasource;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.device.PreferenceManager;
import com.aptatek.pkuapp.domain.interactor.samplewetting.WettingDataSource;
import com.aptatek.pkuapp.util.Constants;

public class WettingDataSourceImpl implements WettingDataSource {

    private final PreferenceManager preferenceManager;

    public WettingDataSourceImpl(@NonNull final PreferenceManager preferenceManager) {
        this.preferenceManager = preferenceManager;
    }

    @Override
    public boolean hasRunningWetting() {
        final long start = preferenceManager.getWettingStart();
        return start > 0L
                && System.currentTimeMillis() - start <= Constants.DEFAULT_WETTING_PERIOD;
    }

    @Override
    public long getWettingStart() {
        return preferenceManager.getWettingStart();
    }

    @Override
    public void startWetting() {
        preferenceManager.setWettingStart(System.currentTimeMillis());
    }

    @Override
    public void stopWetting() {
        preferenceManager.setWettingStart(0L);
    }
}
