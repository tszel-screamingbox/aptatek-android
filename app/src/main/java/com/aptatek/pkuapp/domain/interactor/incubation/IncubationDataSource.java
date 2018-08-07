package com.aptatek.pkuapp.domain.interactor.incubation;

public interface IncubationDataSource {

    boolean hasRunningIncubation();

    long getIncubationStart();

    void startIncubation();

    void stopIncubation();

}
