package com.aptatek.pkulab.view.settings.web;

import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;
import com.aptatek.pkulab.domain.manager.analytic.events.riskmitigation.FaqClosed;
import com.aptatek.pkulab.domain.manager.analytic.events.riskmitigation.FaqPresented;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

public class WebHostPresenter extends MvpBasePresenter<WebHostView> {

    private final IAnalyticsManager analyticsManager;
    private boolean isFaqScreen = false;

    @Inject
    public WebHostPresenter(final IAnalyticsManager analyticsManager) {
        this.analyticsManager = analyticsManager;
    }

    private long attachTimeMs = 0L;

    void initAnalytics(final boolean isFaqScreen) {
        this.isFaqScreen = isFaqScreen;

        if (isFaqScreen && attachTimeMs == 0L) {
            attachTimeMs = System.currentTimeMillis();
            analyticsManager.logEvent(new FaqPresented());
        }
    }

    @Override
    public void detachView() {
        if (!isFaqScreen) {
            return;
        }
        final AnalyticsEvent event = new FaqClosed(System.currentTimeMillis() - attachTimeMs);
        analyticsManager.logEvent(event);
        super.detachView();
    }
}
