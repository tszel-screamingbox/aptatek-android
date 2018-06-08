package com.aptatek.aptatek.domain.interactor.incubation;

public interface IncubationDataSource {

    boolean hasRunningIncubation();

    long getIncubationStart();

    void startIncubation();

    void stopIncubation();

}
