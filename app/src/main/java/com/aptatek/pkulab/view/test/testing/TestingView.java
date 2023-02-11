package com.aptatek.pkulab.view.test.testing;

import com.aptatek.pkulab.view.error.ErrorModel;
import com.aptatek.pkulab.view.test.base.TestFragmentBaseView;

public interface TestingView extends TestFragmentBaseView {

    void showTimeRemaining(final TimeRemaining timeRemaining);

    void onTestFinished(final String testId);

    void showTurnReaderOn();

    void onTestError(final ErrorModel errorModel);

    void setProgressVisible(boolean visible);

    void setProgressPercentage(int percentage);

}
