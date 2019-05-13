package com.aptatek.pkulab.data.datasource;


import androidx.annotation.NonNull;

import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.domain.interactor.wetting.WettingDataSource;
import com.aptatek.pkulab.domain.interactor.wetting.WettingStatus;
import com.aptatek.pkulab.util.Constants;

public class WettingDataSourceImpl implements WettingDataSource {

    private final PreferenceManager preferenceManager;

    public WettingDataSourceImpl(@NonNull final PreferenceManager preferenceManager) {
        this.preferenceManager = preferenceManager;
    }

    @Override
    public WettingStatus getWettingStatus() {
        final long start = preferenceManager.getWettingStart();

        if (start <= 0L) {
            return WettingStatus.NOT_STARTED;
        } else if (System.currentTimeMillis() - start <= Constants.DEFAULT_WETTING_PERIOD) {
            return WettingStatus.RUNNING;
        } else {
            return WettingStatus.FINISHED;
        }
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
    public void resetWetting() {
        preferenceManager.setWettingStart(0L);
    }
}
