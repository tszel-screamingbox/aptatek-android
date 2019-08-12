package com.aptatek.pkulab.view.main;

import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.domain.manager.analytic.events.weekly.WeeklyChartClosed;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

class MainHostActivityPresenter extends MvpBasePresenter<MainHostActivityView> {

    private final IAnalyticsManager analyticsManager;

    @Inject
    MainHostActivityPresenter(IAnalyticsManager analyticsManager) {
        this.analyticsManager = analyticsManager;
    }

    public void logWeeklyChartClosed(final long elapsed) {
        analyticsManager.logEvent(new WeeklyChartClosed(elapsed));
    }
}
