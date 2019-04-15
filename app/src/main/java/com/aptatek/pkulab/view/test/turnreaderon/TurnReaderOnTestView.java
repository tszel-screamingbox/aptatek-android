package com.aptatek.pkulab.view.test.turnreaderon;

import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnView;
import com.aptatek.pkulab.view.test.base.TestFragmentBaseView;

public interface TurnReaderOnTestView extends TestFragmentBaseView, TurnReaderOnView {

    void showTestingScreen();

    void showConnectItAllScreen();

    void showTestResultScreen(String testId);

    void showUsedCassetteError();

}
