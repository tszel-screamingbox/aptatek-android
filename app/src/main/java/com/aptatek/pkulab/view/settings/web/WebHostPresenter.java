package com.aptatek.pkulab.view.settings.web;

import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;
import com.aptatek.pkulab.domain.manager.analytic.events.riskmitigation.FaqClosed;
import com.aptatek.pkulab.domain.manager.analytic.events.riskmitigation.FaqPresented;
import com.aptatek.pkulab.domain.manager.analytic.events.settings.PrivacyDisplayed;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

public class WebHostPresenter extends MvpBasePresenter<WebHostView> {

    private final IAnalyticsManager analyticsManager;
    private boolean isFaqScreen = false;
    private boolean isPrivacyScreen = false;

    @Inject
    public WebHostPresenter(final IAnalyticsManager analyticsManager) {
        this.analyticsManager = analyticsManager;
    }

    private long attachTimeMs = 0L;

    void initFaqAnalytics(final boolean isFaqScreen) {
        this.isFaqScreen = isFaqScreen;

        if (isFaqScreen && attachTimeMs == 0L) {
            attachTimeMs = System.currentTimeMillis();
            analyticsManager.logEvent(new FaqPresented());
        }
    }

    void initPrivacyAnalytics(final boolean isPrivacyScreen) {
        this.isPrivacyScreen = isPrivacyScreen;

        if (isPrivacyScreen && attachTimeMs == 0L) {
            attachTimeMs = System.currentTimeMillis();
        }
    }

    @Override
    public void detachView() {
        if (isPrivacyScreen) {
            final AnalyticsEvent event = new PrivacyDisplayed(System.currentTimeMillis() - attachTimeMs);
            analyticsManager.logEvent(event);
        }

        if (isFaqScreen) {
            final AnalyticsEvent event = new FaqClosed(System.currentTimeMillis() - attachTimeMs);
            analyticsManager.logEvent(event);
        }
        super.detachView();
    }
}
