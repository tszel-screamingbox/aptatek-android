package com.aptatek.pkuapp.injection.module.rangeinfo;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.device.formatter.PkuValueFormatterImpl;
import com.aptatek.pkuapp.device.formatter.RangeSettingsValueFormatterImpl;
import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.view.rangeinfo.PkuValueFormatter;
import com.aptatek.pkuapp.view.settings.pkulevel.RangeSettingsValueFormatter;

import dagger.Module;
import dagger.Provides;

@Module
public class RangeInfoModule {

    @Provides
    PkuValueFormatter providePkuValueFormatter(@NonNull final ResourceInteractor resourceInteractor) {
        return new PkuValueFormatterImpl(resourceInteractor);
    }

    @Provides
    RangeSettingsValueFormatter provideRangeSettingsValueFormatter(@NonNull final ResourceInteractor resourceInteractor) {
        return new RangeSettingsValueFormatterImpl(resourceInteractor);
    }

}
