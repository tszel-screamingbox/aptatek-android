package com.aptatek.pkulab.view.test.testcomplete;

import com.aptatek.pkulab.view.connect.turnreaderon.SyncProgress;
import com.aptatek.pkulab.view.test.base.TestFragmentBaseView;

public interface TestCompleteView extends TestFragmentBaseView {

    void showResults(String testId);

    void showSyncProgressDialog(SyncProgress syncProgress);

    void dismissProgressDialog();
}
