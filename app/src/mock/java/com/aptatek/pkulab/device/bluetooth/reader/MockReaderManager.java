package com.aptatek.pkulab.device.bluetooth.reader;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.data.generator.FakeReaderDataGenerator;
import com.aptatek.pkulab.domain.manager.reader.ReaderManager;
import com.aptatek.pkulab.domain.model.reader.CartridgeInfo;
import com.aptatek.pkulab.domain.model.reader.ConnectionEvent;
import com.aptatek.pkulab.domain.model.reader.Error;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.domain.model.reader.TestProgress;
import com.aptatek.pkulab.domain.model.reader.TestResult;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;

import org.apache.commons.lang3.NotImplementedException;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class MockReaderManager implements ReaderManager {

    private final FakeReaderDataGenerator dataGenerator;

    public MockReaderManager(final FakeReaderDataGenerator dataGenerator) {
        this.dataGenerator = dataGenerator;
    }

    @Override
    public Completable connect(@NonNull final ReaderDevice readerDevice) {
        return Completable.error(new NotImplementedException("dont use it in mock"));
    }

    @Override
    public Completable changeMtu(final int mtuSize) {
        return Completable.error(new NotImplementedException("dont use it in mock"));
    }

    @Override
    public Completable disconnect() {
        return Completable.error(new NotImplementedException("dont use it in mock"));
    }

    @Override
    public Single<Integer> getBatteryLevel() {
        return Single.fromCallable(dataGenerator::generateBatteryLevel)
                .delay(500, TimeUnit.MILLISECONDS);
    }

    @Override
    public Single<CartridgeInfo> getCartridgeInfo() {
        return Single.fromCallable(dataGenerator::generateCartridgeInfo)
                .delay(500, TimeUnit.MILLISECONDS);
    }

    @Override
    public Single<Integer> getNumberOfResults() {
        return Single.fromCallable(dataGenerator::getNumberOfResults)
                .delay(500, TimeUnit.MILLISECONDS);
    }

    @Override
    public Single<TestResult> getResult(@NonNull final String id) {
        return Single.fromCallable(() -> dataGenerator.getResult(id));
    }

    @Override
    public Single<List<TestResult>> syncAllResults() {
        return Single.fromCallable(dataGenerator::getTestResults);
    }

    @Override
    public Single<List<TestResult>> syncResultsAfter(@NonNull final String lastResultId) {
        return Single.fromCallable(dataGenerator::getTestResults);
    }

    @Override
    public Flowable<Integer> batteryLevel() {
        return getBatteryLevel().toFlowable();
    }

    @Override
    public Single<Error> getError() {
        return Single.never(); // no errors for now
    }

    @Override
    public Maybe<ReaderDevice> getConnectedDevice() {
        return Maybe.empty();
    }

    @Override
    public Flowable<ConnectionEvent> connectionEvents() {
        return Flowable.empty();
    }

    @Override
    public Flowable<Integer> mtuSize() {
        return Flowable.empty();
    }

    @Override
    public Flowable<WorkflowState> workflowState() {
        return Flowable.empty();
    }

    @Override
    public Flowable<TestProgress> testProgress() {
        return Flowable.empty();
    }
}
