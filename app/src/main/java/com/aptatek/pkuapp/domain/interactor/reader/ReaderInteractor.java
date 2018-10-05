package com.aptatek.pkuapp.domain.interactor.reader;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.domain.error.ReaderError;
import com.aptatek.pkuapp.domain.manager.reader.ReaderManager;
import com.aptatek.pkuapp.domain.model.ReaderConnectionEvent;
import com.aptatek.pkuapp.domain.model.ReaderDevice;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

public class ReaderInteractor {

    private final ReaderManager readerManager;

    @Inject
    public ReaderInteractor(final ReaderManager readerManager) {
        this.readerManager = readerManager;
    }

    @NonNull
    public Completable connect(@NonNull final ReaderDevice readerDevice) {
        return Completable.fromAction(() -> readerManager.connect(readerDevice))
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
}
