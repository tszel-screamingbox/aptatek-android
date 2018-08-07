package com.aptatek.pkuapp.injection.module.parentalgate;

import com.aptatek.pkuapp.device.formatter.BirthDateFormatterImpl;
import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.domain.interactor.parentalgate.BirthDateFormatter;

import dagger.Module;
import dagger.Provides;

@Module
public class ParentalGateModule {

    @Provides
    BirthDateFormatter provideBirthDateFormatter(final ResourceInteractor resourceInteractor) {
        return new BirthDateFormatterImpl(resourceInteractor);
    }

}
