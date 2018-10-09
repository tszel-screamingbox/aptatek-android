package com.aptatek.pkulab.injection.module.parentalgate;

import com.aptatek.pkulab.device.formatter.BirthDateFormatterImpl;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.parentalgate.BirthDateFormatter;

import dagger.Module;
import dagger.Provides;

@Module
public class ParentalGateModule {

    @Provides
    BirthDateFormatter provideBirthDateFormatter(final ResourceInteractor resourceInteractor) {
        return new BirthDateFormatterImpl(resourceInteractor);
    }

}
