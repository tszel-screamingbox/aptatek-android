package com.aptatek.pkulab.domain.manager.reader;


import androidx.annotation.NonNull;

import com.aptatek.pkulab.device.bluetooth.model.SyncProgress;
import com.aptatek.pkulab.domain.model.reader.CartridgeInfo;
import com.aptatek.pkulab.domain.model.reader.ConnectionEvent;
import com.aptatek.pkulab.domain.model.reader.Error;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.domain.model.reader.TestProgress;
import com.aptatek.pkulab.domain.model.reader.TestResult;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface ReaderManager {

    // on-demand operations / read characteristics
    Completable connect(@NonNull ReaderDevice readerDevice);

    Completable changeMtu(int mtuSize);

    Completable disconnect();

    Single<Integer> getBatteryLevel();

    Single<CartridgeInfo> getCartridgeInfo();

    Single<Integer> getNumberOfResults();

    Single<TestResult> getResult(@NonNull String id);

    Single<List<TestResult>> syncAllResults();

    Single<List<TestResult>> syncResultsAfter(@NonNull String lastResultId);

    Flowable<TestResult> syncAllResultsFlowable();

    Flowable<TestResult> syncResultsAfterFlowable(@NonNull String lastResultId);

    Single<Error> getError();

    Maybe<ReaderDevice> getConnectedDevice();

    // hot observables / notify characteristics
    Flowable<ConnectionEvent> connectionEvents();

    Flowable<Integer> mtuSize();

    Flowable<WorkflowState> workflowState(String debug);

    Flowable<TestProgress> testProgress();

    Flowable<Integer> batteryLevel();

    Flowable<SyncProgress> getSyncProgressFlowable();

}
