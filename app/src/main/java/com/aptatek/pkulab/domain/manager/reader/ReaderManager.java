package com.aptatek.pkulab.domain.manager.reader;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.domain.model.reader.CartridgeInfo;
import com.aptatek.pkulab.domain.model.reader.ConnectionEvent;
import com.aptatek.pkulab.domain.model.reader.Error;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.domain.model.reader.TestResult;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
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

    Single<List<TestResult>> syncResults();

    Single<Error> getError();

    // hot observables / notify characteristics
    Flowable<ConnectionEvent> connectionEvents();

    Flowable<Integer> mtuSize();

    Flowable<WorkflowState> workflowState();

}
