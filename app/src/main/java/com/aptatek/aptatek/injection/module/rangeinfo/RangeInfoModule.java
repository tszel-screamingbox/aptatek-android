package com.aptatek.aptatek.injection.module.rangeinfo;

import android.support.annotation.NonNull;

import com.aptatek.aptatek.data.datasource.PkuRangeDataSourceImpl;
import com.aptatek.aptatek.device.PreferenceManager;
import com.aptatek.aptatek.device.formatter.PkuValueFormatterImpl;
import com.aptatek.aptatek.device.formatter.RangeSettingsValueFormatterImpl;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.interactor.pkurange.PkuRangeDataSource;
import com.aptatek.aptatek.view.rangeinfo.PkuValueFormatter;
import com.aptatek.aptatek.view.settings.pkulevel.RangeSettingsValueFormatter;

import dagger.Module;
import dagger.Provides;

@Module
public class RangeInfoModule {

    @Provides
    PkuRangeDataSource providePkuRangeDataSource(@NonNull final PreferenceManager preferenceManager) {
        return new PkuRangeDataSourceImpl(preferenceManager);
    }

    @Provides
    PkuValueFormatter providePkuValueFormatter(@NonNull final ResourceInteractor resourceInteractor) {
        return new PkuValueFormatterImpl(resourceInteractor);
    }

    @Provides
    RangeSettingsValueFormatter provideRangeSettingsValueFormatter(@NonNull final ResourceInteractor resourceInteractor) {
        return new RangeSettingsValueFormatterImpl(resourceInteractor);
    }

}
