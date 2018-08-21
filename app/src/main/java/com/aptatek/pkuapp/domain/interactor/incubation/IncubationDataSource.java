package com.aptatek.pkuapp.domain.interactor.incubation;

public interface IncubationDataSource {

    IncubationStatus getIncubationStatus();

    long getIncubationStart();

    void startIncubation();

    void resetIncubation();

    void skipIncubation();

}
