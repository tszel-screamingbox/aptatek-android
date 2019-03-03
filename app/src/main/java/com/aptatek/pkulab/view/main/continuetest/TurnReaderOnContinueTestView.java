package com.aptatek.pkulab.view.main.continuetest;

import com.aptatek.pkulab.domain.model.ContinueTestResultType;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnView;

interface TurnReaderOnContinueTestView extends TurnReaderOnView {
    void finishTestContinue(final ContinueTestResultType continueTestResultType);
}
