package com.aptatek.aptatek.data;

import android.support.annotation.NonNull;

import com.aptatek.aptatek.device.PreferenceManager;
import com.aptatek.aptatek.domain.interactor.samplewetting.WettingDataSource;
import com.aptatek.aptatek.util.Constants;

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
