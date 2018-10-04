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

    public Completable connect(@NonNull ReaderDevice readerDevice) {
        return Completable.fromAction(() -> readerManager.connect(readerDevice))
                .subscribeOn(Schedulers.io());
    }

    public Completable disconnect() {
        return Completable.fromAction(readerManager::disconnect)
                .subscribeOn(Schedulers.io());
    }

    public Flowable<ReaderConnectionEvent> getReaderConnectionState() {
        return readerManager.connectionEvents();
    }

    public Flowable<ReaderError> getReaderError() {
        return readerManager.readerErrors();
    }

    public Flowable<Integer> getBatteryLevel() {
        return readerManager.batteryLevel();
    }
}
