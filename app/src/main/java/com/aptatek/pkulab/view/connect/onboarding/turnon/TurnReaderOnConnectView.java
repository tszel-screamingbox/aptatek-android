package com.aptatek.pkulab.view.connect.onboarding.turnon;

import com.aptatek.pkulab.view.connect.onboarding.common.BaseConnectOnboardingScreenView;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnView;

public interface TurnReaderOnConnectView extends BaseConnectOnboardingScreenView, TurnReaderOnView {

    void navigateToHome();
    void displaySkipButton();

}
