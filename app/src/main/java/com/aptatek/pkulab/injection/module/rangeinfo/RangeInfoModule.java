package com.aptatek.pkulab.injection.module.rangeinfo;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.device.formatter.PkuValueFormatterImpl;
import com.aptatek.pkulab.device.formatter.RangeSettingsValueFormatterImpl;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.view.rangeinfo.PkuValueFormatter;
import com.aptatek.pkulab.view.settings.pkulevel.RangeSettingsValueFormatter;

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
