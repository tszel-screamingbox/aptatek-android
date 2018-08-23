package com.aptatek.pkuapp.domain.interactor.samplewetting;

public interface WettingDataSource {

    WettingStatus getWettingStatus();

    long getWettingStart();

    void startWetting();

    void resetWetting();

}
