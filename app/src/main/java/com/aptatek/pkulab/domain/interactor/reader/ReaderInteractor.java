package com.aptatek.pkulab.domain.interactor.reader;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.data.AptatekDatabase;
import com.aptatek.pkulab.domain.interactor.cube.TestResultRepository;
import com.aptatek.pkulab.domain.manager.reader.ReaderManager;
import com.aptatek.pkulab.domain.model.reader.ConnectionEvent;
import com.aptatek.pkulab.domain.model.reader.Error;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.domain.model.reader.TestResult;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class ReaderInteractor {

    private final ReaderManager readerManager;
    private final TestResultRepository testResultRepository;

    @Inject
    public ReaderInteractor(final ReaderManager readerManager,
                            final TestResultRepository testResultRepository) {
        this.readerManager = readerManager;
        this.testResultRepository = testResultRepository;
    }

    @NonNull
    public Completable connect(@NonNull final ReaderDevice readerDevice) {
        return readerManager.connect(readerDevice)
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
    public Single<String> queryCartridgeId() {
        return readerManager.getCartridgeId()
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
}
