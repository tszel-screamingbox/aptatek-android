package com.aptatek.aptatek.injection.module.parentalgate;

import com.aptatek.aptatek.device.formatter.BirthDateFormatterImpl;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.interactor.parentalgate.BirthDateFormatter;

import dagger.Module;
import dagger.Provides;

@Module
public class ParentalGateModule {

    @Provides
    BirthDateFormatter provideBirthDateFormatter(final ResourceInteractor resourceInteractor) {
        return new BirthDateFormatterImpl(resourceInteractor);
    }

}
