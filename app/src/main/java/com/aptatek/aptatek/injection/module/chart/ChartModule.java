package com.aptatek.aptatek.injection.module.chart;

import com.aptatek.aptatek.data.datasource.FakeCubeDataGenerator;
import com.aptatek.aptatek.data.datasource.FakeCubeDataSourceImpl;
import com.aptatek.aptatek.device.formatter.WeeklyChartDateFormatterImpl;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.interactor.cube.CubeDataSource;
import com.aptatek.aptatek.view.weekly.WeeklyChartDateFormatter;

import dagger.Module;
import dagger.Provides;

@Module
public class ChartModule {

    @Provides
    public WeeklyChartDateFormatter provideWeeklyChartDateFormatter(final ResourceInteractor resourceInteractor) {
        return new WeeklyChartDateFormatterImpl(resourceInteractor);
    }

    @Provides
    public CubeDataSource provideCubeDataSource(final FakeCubeDataGenerator dataGenerator) {
        return new FakeCubeDataSourceImpl(dataGenerator);
    }

}
