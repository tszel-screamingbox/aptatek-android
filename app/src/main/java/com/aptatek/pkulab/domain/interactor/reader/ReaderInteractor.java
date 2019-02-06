package com.aptatek.pkulab.domain.interactor.reader;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.domain.interactor.testresult.TestResultRepository;
import com.aptatek.pkulab.domain.manager.reader.ReaderManager;
import com.aptatek.pkulab.domain.model.reader.CartridgeInfo;
import com.aptatek.pkulab.domain.model.reader.ConnectionEvent;
import com.aptatek.pkulab.domain.model.reader.Error;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.domain.model.reader.TestResult;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class ReaderInteractor {

    private final PreferenceManager preferenceManager;
    private final ReaderManager readerManager;
    private final TestResultRepository testResultRepository;

    @Inject
    public ReaderInteractor(final PreferenceManager preferenceManager,
                            final ReaderManager readerManager,
                            final TestResultRepository testResultRepository) {
        this.preferenceManager = preferenceManager;
        this.readerManager = readerManager;
        this.testResultRepository = testResultRepository;
    }

    @NonNull
    public Completable connect(@NonNull final ReaderDevice readerDevice) {
        return readerManager.connect(readerDevice)
                .andThen(Completable.fromAction(() -> preferenceManager.setPairedDevice(readerDevice.getMac())))
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    public Completable disconnect() {
        return readerManager.disconnect()
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    public Single<Integer> getBatteryLevel() {
        return readerManager.getBatteryLevel()
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    public Single<CartridgeInfo> getCartridgeInfo() {
        return readerManager.getCartridgeInfo()
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    public Single<List<TestResult>> syncResults() {
        return readerManager.syncResults()
                .flatMap(results -> testResultRepository.insertAll(results)
                        .andThen(Single.just(results))
                )
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    public Single<TestResult> getResult(@NonNull final String id) {
        return readerManager.getResult(id)
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    public Single<Error> getError() {
        return readerManager.getError()
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    public Flowable<ConnectionEvent> getReaderConnectionEvents() {
        return readerManager.connectionEvents()
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    public Flowable<Integer> getMtuSize() {
        return readerManager.mtuSize()
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    public Flowable<WorkflowState> getWorkflowState() {
        return readerManager.workflowState()
                .subscribeOn(Schedulers.io());
    }

    public Maybe<ReaderDevice> getConnectedReader() {
        return readerManager.getConnectedDevice()
                .subscribeOn(Schedulers.io());
    }

    public Maybe<String> getLastConnectedMac() {
        final String pairedDevice = preferenceManager.getPairedDevice();

        if (pairedDevice == null) {
            return Maybe.empty();
        }

        return Maybe.just(pairedDevice);
    }
}
