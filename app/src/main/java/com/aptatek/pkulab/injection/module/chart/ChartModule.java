package com.aptatek.pkulab.injection.module.chart;

import com.aptatek.pkulab.device.formatter.WeeklyChartResourceFormatterImpl;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.view.main.weekly.WeeklyChartResourceFormatter;

import dagger.Module;
import dagger.Provides;

@Module
public class ChartModule {

    @Provides
    public WeeklyChartResourceFormatter provideWeeklyChartDateFormatter(final ResourceInteractor resourceInteractor) {
        return new WeeklyChartResourceFormatterImpl(resourceInteractor);
    }
}
