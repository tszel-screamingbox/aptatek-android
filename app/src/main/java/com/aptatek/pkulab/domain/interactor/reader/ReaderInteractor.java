package com.aptatek.pkulab.domain.interactor.reader;


import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.domain.interactor.testresult.TestResultRepository;
import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.domain.manager.analytic.events.readerconnection.ReaderDataSynced;
import com.aptatek.pkulab.domain.manager.analytic.events.riskmitigation.UnfinishedTest;
import com.aptatek.pkulab.domain.manager.reader.ReaderManager;
import com.aptatek.pkulab.domain.model.reader.CartridgeInfo;
import com.aptatek.pkulab.domain.model.reader.ConnectionEvent;
import com.aptatek.pkulab.domain.model.reader.Error;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.domain.model.reader.TestProgress;
import com.aptatek.pkulab.domain.model.reader.TestResult;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;
import com.aptatek.pkulab.view.connect.turnreaderon.SyncProgress;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import ix.Ix;
import timber.log.Timber;

public class ReaderInteractor {

    private final PreferenceManager preferenceManager;
    private final ReaderManager readerManager;
    private final TestResultRepository testResultRepository;
    private final IAnalyticsManager analyticsManager;
    private long syncStartedAtMs = -1L;

    @Inject
    public ReaderInteractor(final PreferenceManager preferenceManager,
                            final ReaderManager readerManager,
                            final TestResultRepository testResultRepository,
                            final IAnalyticsManager analyticsManager) {
        this.preferenceManager = preferenceManager;
        this.readerManager = readerManager;
        this.testResultRepository = testResultRepository;
        this.analyticsManager = analyticsManager;
    }

    @NonNull
    public Completable connect(@NonNull final ReaderDevice readerDevice) {
        return readerManager.connect(readerDevice)
                .andThen(Completable.fromAction(() -> preferenceManager.setPairedDevice(readerDevice.getMac())))
                .andThen(Completable.fromAction(() -> preferenceManager.setPairedDeviceName(readerDevice.getName())))
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

    public Completable syncAllResultsCompletable() {
        return readerManager.syncAllResultsFlowable()
                .flatMapCompletable(testResultRepository::insert).onErrorComplete()
                .subscribeOn(Schedulers.io());
    }

    public Completable syncResultsAfterLast() {
        return readerManager.getConnectedDevice()
                .toSingle()
                .map(ReaderDevice::getMac)
                .flatMap(testResultRepository::getLatestFromReader)
                .map(TestResult::getId)
                .onErrorReturnItem("invalid")
                .toFlowable()
                .flatMap(id -> {
                    if (id.equals("invalid")) {
                        return readerManager.syncAllResultsFlowable();
                    } else {
                        return readerManager.syncResultsAfterFlowable(id);
                    }
                })
                .flatMapCompletable(testResultRepository::insert)
                .onErrorComplete()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io());
    }

    public Completable syncMissingRecords() {
        return readerManager.getConnectedDevice()
                .toSingle()
                .map(ReaderDevice::getMac)
                .flatMapCompletable(mac -> testResultRepository.listAll()
                        .doOnNext(a -> Timber.wtf("--- dbRecords=%s", Arrays.toString(Ix.from(a).map(TestResult::getId).toList().toArray())))
                        .take(1)
                        .singleOrError()
                        .map(records -> Ix.from(records)
                                .filter(result -> TextUtils.equals(mac, result.getReaderMac()))
                                .map(TestResult::getId)
                                .toList())
                        .onErrorReturnItem(Collections.emptyList())
                        .flatMapCompletable(idsInDb -> readerManager.syncResultsExcludingList(idsInDb)
                                .map(result -> result.toBuilder().setReaderMac(mac).build())
                                .doOnNext(tr -> Timber.wtf("--- record to be inserted=%s", tr))
                                .flatMapCompletable(testResultRepository::insert).onErrorComplete())
                )
                .subscribeOn(Schedulers.io());
    }

    public Flowable<SyncProgress> syncProgressFlowable() {
        return readerManager.getSyncProgressFlowable().map(sp -> SyncProgress.create(sp.getCurrentResults(), sp.getFailedResults(), sp.getTotalResults()));
    }

    @NonNull
    public Single<List<TestResult>> syncResultsAfterLatest() {
        return getLastConnectedMac().toSingle()
                .flatMap(testResultRepository::getLatestFromReader)
                .map(TestResult::getId)
                .onErrorReturnItem("invalid")
                .flatMap(id -> {
                    if (id.equals("invalid")) {
                        return readerManager.syncAllResults();
                    } else {
                        analyticsManager.logEvent(new UnfinishedTest("risk_unfinished_test_show_result"));
                        return readerManager.syncResultsAfter(id);
                    }
                })
                .doOnSuccess(a -> Timber.d("--- syncResultsAfterLatest synced %d", a.size()))
                .doOnError(a -> Timber.d("--- syncResultsAfterLatest error %s", a))
                .flatMap(results -> testResultRepository.insertAll(results)
                        .andThen(Single.just(results))
                )
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    public Single<TestResult> readAndStoreResult() {
        return readerManager.readResult()
                .flatMap(testResult ->
                        readerManager.getConnectedDevice()
                                .map(ReaderDevice::getMac)
                                .toSingle()
                                .onErrorReturnItem("")
                                .map(mac -> testResult.toBuilder().setReaderMac(mac).build())
                                .flatMap(resultToInsert ->
                                        testResultRepository.insert(resultToInsert)
                                                .andThen(Single.just(resultToInsert))
                                )
                )
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    public Single<TestResult> getResult(@NonNull final String id) {
        return getResult(id, false);
    }

    @NonNull
    public Single<TestResult> getResult(@NonNull final String id, final boolean readMac) {
        return readerManager.getResult(id)
                .flatMap(result -> readMac ? readerManager.getConnectedDevice().toSingle().map(ReaderDevice::getMac).map(mac -> result.toBuilder().setReaderMac(mac).build()) : Single.just(result))
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
    public Flowable<WorkflowState> getWorkflowState(String debug) {
        return readerManager.workflowState(debug)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    @NonNull
    public Flowable<Integer> batteryLevelUpdates() {
        return readerManager.batteryLevel()
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    public Maybe<ReaderDevice> getConnectedReader() {
        return readerManager.getConnectedDevice()
                .subscribeOn(Schedulers.io());
        //.doOnSuccess(reader -> analyticsManager.logEvent(new DeviceInfoRead(reader.getFirmwareVersion(), reader.getSerial())));
    }

    @NonNull
    public Maybe<String> getLastConnectedMac() {
        final String pairedDevice = preferenceManager.getPairedDevice();

        if (pairedDevice == null) {
            return Maybe.empty();
        }

        return Maybe.just(pairedDevice);
    }


    public Maybe<String> getLastConnectedName() {
        final String pairedDevice = preferenceManager.getPairedDeviceName();

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
