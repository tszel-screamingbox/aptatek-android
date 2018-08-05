package com.aptatek.aptatek.injection.module.chart;

import com.aptatek.aptatek.device.formatter.WeeklyChartDateFormatterImpl;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.view.weekly.WeeklyChartDateFormatter;

import dagger.Module;
import dagger.Provides;

@Module
public class ChartModule {

    @Provides
    public WeeklyChartDateFormatter provideWeeklyChartDateFormatter(final ResourceInteractor resourceInteractor) {
        return new WeeklyChartDateFormatterImpl(resourceInteractor);
    }
}
