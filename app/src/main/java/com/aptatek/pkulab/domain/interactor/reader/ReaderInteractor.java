package com.aptatek.pkulab.domain.interactor.reader;


import androidx.annotation.NonNull;

import com.aptatek.pkulab.device.DeviceHelper;
import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.domain.interactor.testresult.TestResultRepository;
import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.domain.manager.analytic.events.readerconnection.DeviceInfoRead;
import com.aptatek.pkulab.domain.manager.analytic.events.readerconnection.ReaderDataSynced;
import com.aptatek.pkulab.domain.manager.analytic.events.readerconnection.ReaderWorkflowStateError;
import com.aptatek.pkulab.domain.manager.analytic.events.readerconnection.WorkflowStateChanged;
import com.aptatek.pkulab.domain.manager.analytic.events.test.ReaderSelfTestFinished;
import com.aptatek.pkulab.domain.manager.reader.ReaderManager;
import com.aptatek.pkulab.domain.model.reader.CartridgeInfo;
import com.aptatek.pkulab.domain.model.reader.ConnectionEvent;
import com.aptatek.pkulab.domain.model.reader.Error;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.domain.model.reader.TestProgress;
import com.aptatek.pkulab.domain.model.reader.TestResult;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class ReaderInteractor {

    private final PreferenceManager preferenceManager;
    private final ReaderManager readerManager;
    private final TestResultRepository testResultRepository;
    private final IAnalyticsManager analyticsManager;
    private long syncStartedAtMs = -1L;
    private WorkflowState lastWorkflowState = WorkflowState.DEFAULT;
    private long selfTestStartedAtMs = -1L;
    private final DeviceHelper deviceHelper;

    @Inject
    public ReaderInteractor(final PreferenceManager preferenceManager,
                            final ReaderManager readerManager,
                            final TestResultRepository testResultRepository,
                            final IAnalyticsManager analyticsManager,
                            final DeviceHelper deviceHelper) {
        this.preferenceManager = preferenceManager;
        this.readerManager = readerManager;
        this.testResultRepository = testResultRepository;
        this.analyticsManager = analyticsManager;
        this.deviceHelper = deviceHelper;
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

    // leaving this here for force update scenario, maybe useful in the future...
    @NonNull
    public Single<List<TestResult>> syncAllResults() {
        return readerManager.syncAllResults()
                .doOnSubscribe(ignored -> syncStartedAtMs = System.currentTimeMillis())
                .flatMap(results -> testResultRepository.insertAll(results)
                        .andThen(Single.just(results))
                )
                .observeOn(Schedulers.io())
                .doOnSuccess(list -> {
                    try {
                        final ReaderDevice device = getConnectedReader().blockingGet();
                        analyticsManager.logEvent(new ReaderDataSynced(list.size(), Math.abs(System.currentTimeMillis() - syncStartedAtMs), device.getSerial(), device.getFirmwareVersion()));
                        syncStartedAtMs = -1L;
                    } catch (final Exception e) {
                        Timber.d("Failed to report ReaderDataSynced event: %s", e);
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    public Single<List<TestResult>> syncResultsAfterLatest() {
        return readerManager.getConnectedDevice()
                .toSingle()
                .doOnSubscribe(ignored -> syncStartedAtMs = System.currentTimeMillis())
                .map(ReaderDevice::getMac)
                .flatMap(testResultRepository::getLatestFromReader)
                .map(TestResult::getId)
                .onErrorReturnItem("invalid")
                .flatMap(id -> (id.equals("invalid")) ? readerManager.syncAllResults() : readerManager.syncResultsAfter(id))
                .flatMap(results -> testResultRepository.insertAll(results)
                        .andThen(Single.just(results))
                )
                .observeOn(Schedulers.io())
                .doOnSuccess(list -> {
                    try {
                        final ReaderDevice device = getConnectedReader().blockingGet();
                        analyticsManager.logEvent(new ReaderDataSynced(list.size(), Math.abs(System.currentTimeMillis() - syncStartedAtMs), device.getSerial(), device.getFirmwareVersion()));
                        syncStartedAtMs = -1L;
                    } catch (final Exception e) {
                        Timber.d("Failed to report ReaderDataSynced event: %s", e);
                    }
                })
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
                .subscribeOn(Schedulers.io())
                .flatMap(error -> getConnectedReader().toSingle()
                        .doOnSuccess(device -> analyticsManager.logEvent(new com.aptatek.pkulab.domain.manager.analytic.events.readerconnection.ReaderError(device.getSerial(), device.getFirmwareVersion())))
                        .map(device -> error)
                        .onErrorResumeNext(Single.just(error))
                );
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
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(state -> {
                    try {
                        final ReaderDevice device = getConnectedReader().blockingGet();
                        if (state == WorkflowState.USED_CASSETTE_ERROR) {
                            analyticsManager.logEvent(new ReaderWorkflowStateError(device.getSerial(), device.getFirmwareVersion()));
                        }

                        if (state == WorkflowState.SELF_TEST) {
                            selfTestStartedAtMs = System.currentTimeMillis();
                        }

                        if (lastWorkflowState == WorkflowState.SELF_TEST) {
                            analyticsManager.logEvent(new ReaderSelfTestFinished(Math.abs(System.currentTimeMillis() - selfTestStartedAtMs), deviceHelper.getPhoneBattery()));
                            selfTestStartedAtMs = -1L;
                        }

                        analyticsManager.logEvent(new WorkflowStateChanged(lastWorkflowState.toString(), state.toString(), device.getFirmwareVersion(), device.getSerial()));
                        lastWorkflowState = state;
                    } catch (final Exception e) {
                        Timber.d("Failed to get connected device: %s", e);
                    }

                });
    }

    @NonNull
    public Flowable<Integer> batteryLevelUpdates() {
        return readerManager.batteryLevel()
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    public Maybe<ReaderDevice> getConnectedReader() {
        return readerManager.getConnectedDevice()
                .subscribeOn(Schedulers.io())
                .doOnSuccess(reader -> analyticsManager.logEvent(new DeviceInfoRead(reader.getFirmwareVersion(), reader.getSerial())));
    }

    @NonNull
    public Maybe<String> getLastConnectedMac() {
        final String pairedDevice = preferenceManager.getPairedDevice();

        if (pairedDevice == null) {
            return Maybe.empty();
        }

        return Maybe.just(pairedDevice);
    }

    @NonNull
    public Flowable<TestProgress> getTestProgress() {
        return readerManager.testProgress()
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    public Completable saveResult(@NonNull final TestResult testResult) {
        return testResultRepository.insertAll(Collections.singletonList(testResult))
                .subscribeOn(Schedulers.io());
    }

    public Single<Integer> getNumberOfResults() {
        return readerManager.getNumberOfResults();
    }
}
