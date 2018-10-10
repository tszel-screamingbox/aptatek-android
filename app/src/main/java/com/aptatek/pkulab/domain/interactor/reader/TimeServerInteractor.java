package com.aptatek.pkulab.domain.interactor.reader;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.domain.error.TimeServerError;
import com.aptatek.pkulab.domain.manager.reader.TimeServer;
import com.aptatek.pkulab.domain.manager.reader.TimeServerCallbacks;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

public class TimeServerInteractor {

    private final TimeServer timeServer;

    @Inject
    public TimeServerInteractor(final TimeServer timeServer) {
        this.timeServer = timeServer;
    }

    @NonNull
    public Completable startTimeServer() {
        return Completable.create(emitter ->
                timeServer.startServer(new TimeServerCallbacks() {
                    @Override
                    public void onOperationSuccessful() {
                        emitter.onComplete();
                    }

                    @Override
                    public void onOperationFailed(@NonNull final TimeServerError error) {
                        emitter.onError(error);
                    }
                })
        ).subscribeOn(Schedulers.io());
    }

    @NonNull
    public Completable stopTimeServer() {
        return Completable.create(emitter ->
                timeServer.stopServer(new TimeServerCallbacks() {
                    @Override
                    public void onOperationSuccessful() {
                        emitter.onComplete();
                    }

                    @Override
                    public void onOperationFailed(@NonNull final TimeServerError error) {
                        emitter.onError(error);
                    }
                })
        ).subscribeOn(Schedulers.io());
    }
}
