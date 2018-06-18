package com.aptatek.aptatek.domain.interactor.wetting;

public interface WettingDataSource {

    boolean hasRunningWetting();

    long getWettingStart();

    void startWetting();

    void stopWetting();

}
