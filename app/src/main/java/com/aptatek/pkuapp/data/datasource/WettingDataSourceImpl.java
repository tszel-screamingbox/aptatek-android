package com.aptatek.pkuapp.data.datasource;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.device.PreferenceManager;
import com.aptatek.pkuapp.domain.interactor.samplewetting.WettingDataSource;
import com.aptatek.pkuapp.domain.interactor.samplewetting.WettingStatus;
import com.aptatek.pkuapp.util.Constants;

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
