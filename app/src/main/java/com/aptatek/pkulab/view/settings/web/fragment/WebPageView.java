package com.aptatek.pkulab.view.settings.web.fragment;

import com.aptatek.pkulab.domain.model.ReportIssue;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface WebPageView extends MvpView {

    void sendIssueEmail(final ReportIssue reportIssue);
}
