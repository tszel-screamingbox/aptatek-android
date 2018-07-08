package com.aptatek.aptatek.domain.interactor.samplewetting;

public interface WettingDataSource {

    boolean hasRunningWetting();

    long getWettingStart();

    void startWetting();

    void stopWetting();

}
