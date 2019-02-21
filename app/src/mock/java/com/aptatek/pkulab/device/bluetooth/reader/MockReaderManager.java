package com.aptatek.pkulab.device.bluetooth.reader;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.data.generator.FakeReaderDataGenerator;
import com.aptatek.pkulab.domain.manager.reader.ReaderManager;
import com.aptatek.pkulab.domain.model.reader.CartridgeInfo;
import com.aptatek.pkulab.domain.model.reader.ConnectionEvent;
import com.aptatek.pkulab.domain.model.reader.ConnectionState;
import com.aptatek.pkulab.domain.model.reader.Error;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.domain.model.reader.TestProgress;
import com.aptatek.pkulab.domain.model.reader.TestResult;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.processors.BehaviorProcessor;
import io.reactivex.processors.FlowableProcessor;

public class MockReaderManager implements ReaderManager {

    private final FlowableProcessor<ConnectionEvent> connectionEventProcessor = BehaviorProcessor.createDefault(ConnectionEvent.create(null, ConnectionState.DISCONNECTED));
    private final FlowableProcessor<Integer> mtuSizeProcessor = BehaviorProcessor.create();
    private final FlowableProcessor<WorkflowState> workflowStateProcessor = BehaviorProcessor.create();
    private final FlowableProcessor<TestProgress> testProgressProcessor = BehaviorProcessor.create();

    private volatile ReaderDevice connectedDevice;

    private final FakeReaderDataGenerator dataGenerator;

    public MockReaderManager(final FakeReaderDataGenerator dataGenerator) {
        this.dataGenerator = dataGenerator;
    }

    @Override
    public Completable connect(@NonNull final ReaderDevice readerDevice) {
        return Completable.complete()
                .delay(1, TimeUnit.SECONDS)
                .doOnComplete(() -> {
                    connectedDevice = readerDevice;
                    Flowable.fromArray(ConnectionState.CONNECTING, ConnectionState.CONNECTED, ConnectionState.READY)
                            .delay(500, TimeUnit.MILLISECONDS)
                            .map(state -> ConnectionEvent.create(readerDevice, state))
                            .subscribe(connectionEventProcessor::onNext);
                });
    }

    @Override
    public Completable changeMtu(final int mtuSize) {
        return Completable.complete()
                .delay(1, TimeUnit.SECONDS)
                .doOnComplete(() -> mtuSizeProcessor.onNext(mtuSize));
    }

    @Override
    public Completable disconnect() {
        return Completable.complete()
                .delay(1, TimeUnit.SECONDS)
                .doOnComplete(() -> {
                    Flowable.fromArray(ConnectionState.DISCONNECTING, ConnectionState.DISCONNECTED)
                            .delay(500, TimeUnit.MILLISECONDS)
                            .map(state -> ConnectionEvent.create(connectedDevice, state))
                            .subscribe(connectionEventProcessor::onNext);
                    connectedDevice = null;
                });
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
    public Single<List<TestResult>> syncResults() {
        return Single.fromCallable(dataGenerator::getTestResults)
                .delay(5, TimeUnit.SECONDS);
    }

    @Override
    public Single<Error> getError() {
        return Single.never(); // no errors for now
    }

    @Override
    public Maybe<ReaderDevice> getConnectedDevice() {
        return Maybe.create(emitter -> {
            if (connectedDevice == null) {
                emitter.onComplete();
            }

            emitter.onSuccess(connectedDevice);
        });
    }

    @Override
    public Flowable<ConnectionEvent> connectionEvents() {
        return connectionEventProcessor;
    }

    @Override
    public Flowable<Integer> mtuSize() {
        return mtuSizeProcessor;
    }

    @Override
    public Flowable<WorkflowState> workflowState() {
        return workflowStateProcessor;
    }

    @Override
    public Flowable<TestProgress> testProgress() {
        return testProgressProcessor;
    }
}
