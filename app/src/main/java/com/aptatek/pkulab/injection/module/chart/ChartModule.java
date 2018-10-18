package com.aptatek.pkulab.injection.module.chart;

import com.aptatek.pkulab.device.formatter.WeeklyChartDateFormatterImpl;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.view.main.weekly.WeeklyChartDateFormatter;

import dagger.Module;
import dagger.Provides;

@Module
public class ChartModule {

    @Provides
    public WeeklyChartDateFormatter provideWeeklyChartDateFormatter(final ResourceInteractor resourceInteractor) {
        return new WeeklyChartDateFormatterImpl(resourceInteractor);
    }
}
