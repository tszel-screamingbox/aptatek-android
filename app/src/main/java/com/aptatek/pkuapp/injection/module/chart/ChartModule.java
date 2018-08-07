package com.aptatek.pkuapp.injection.module.chart;

import com.aptatek.pkuapp.device.formatter.WeeklyChartDateFormatterImpl;
import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.view.weekly.WeeklyChartDateFormatter;

import dagger.Module;
import dagger.Provides;

@Module
public class ChartModule {

    @Provides
    public WeeklyChartDateFormatter provideWeeklyChartDateFormatter(final ResourceInteractor resourceInteractor) {
        return new WeeklyChartDateFormatterImpl(resourceInteractor);
    }
}
