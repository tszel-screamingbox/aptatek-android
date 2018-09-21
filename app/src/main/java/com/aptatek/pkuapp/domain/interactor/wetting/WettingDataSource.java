package com.aptatek.pkuapp.domain.interactor.wetting;

public interface WettingDataSource {

    WettingStatus getWettingStatus();

    long getWettingStart();

    void startWetting();

    void resetWetting();

}
