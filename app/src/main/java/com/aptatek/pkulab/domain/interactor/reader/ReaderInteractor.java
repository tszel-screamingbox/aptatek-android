package com.aptatek.pkulab.domain.interactor.reader;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.device.bluetooth.LumosReaderConstants;
import com.aptatek.pkulab.domain.error.ReaderError;
import com.aptatek.pkulab.domain.manager.reader.ReaderManager;
import com.aptatek.pkulab.domain.model.ReaderConnectionEvent;
import com.aptatek.pkulab.domain.model.ReaderDevice;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.schedulers.Schedulers;

public class ReaderInteractor {

    private final ReaderManager readerManager;

    @Inject
    public ReaderInteractor(final ReaderManager readerManager) {
        this.readerManager = readerManager;
    }

    @NonNull
    public Completable connect(@NonNull final ReaderDevice readerDevice) {
        return connect(readerDevice, LumosReaderConstants.MTU_SIZE);
    }

    @NonNull
    public Completable connect(@NonNull final ReaderDevice readerDevice, final int mtuSize) {
        return Completable.fromAction(() -> readerManager.connect(readerDevice, mtuSize))
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    public Completable disconnect() {
        return Completable.fromAction(readerManager::disconnect)
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    public Completable queryBatteryLevel() {
        return Completable.fromAction(readerManager::queryBatteryLevel)
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    public Completable queryCartridgeId() {
        return Completable.fromAction(readerManager::queryCartridgeId)
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    public Flowable<ReaderConnectionEvent> getReaderConnectionEvents() {
        return readerManager.connectionEvents();
    }

    @NonNull
    public Flowable<ReaderError> getReaderError() {
        return readerManager.readerErrors();
    }

    @NonNull
    public Flowable<Integer> getBatteryLevel() {
        return readerManager.batteryLevel();
    }

    @NonNull
    public Flowable<Integer> getMtuSize() {
        return readerManager.mtuSize();
    }
}
