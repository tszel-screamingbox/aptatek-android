package com.aptatek.pkulab.injection.module;

import com.aptatek.pkulab.domain.manager.analytic.AnalyticsManager;
import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class AnalyticsModule {

    @Binds
    public abstract IAnalyticsManager analyticsManager(AnalyticsManager analyticsManager);
}
