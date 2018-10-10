package com.aptatek.pkulab.view.connect.connected;

import android.content.Context;
import android.util.Pair;

import com.aptatek.pkulab.domain.interactor.reader.ReaderInteractor;
import com.aptatek.pkulab.domain.model.ReaderConnectionState;
import com.aptatek.pkulab.injection.qualifier.ActivityContext;
import com.aptatek.pkulab.view.connect.common.BaseConnectScreenPresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class ConnectedPresenter extends BaseConnectScreenPresenter<ConnectedView> {

    private final ReaderInteractor readerInteractor;

    private CompositeDisposable disposables;

    @Inject
    public ConnectedPresenter(@ActivityContext final Context context,
                              final ReaderInteractor readerInteractor) {
        super(context);
        this.readerInteractor = readerInteractor;
    }

    @Override
    public void attachView(final ConnectedView view) {
        super.attachView(view);

        disposeSubscriptions();

        disposables = new CompositeDisposable();

        disposables.add(
                readerInteractor.getReaderConnectionEvents()
                        .filter(event -> event.getConnectionState() == ReaderConnectionState.READY)
                        .take(1)
                        .flatMap(event -> readerInteractor.queryBatteryLevel()
                                .andThen(readerInteractor.getBatteryLevel()
                                        .take(1)
                                        .map(batteryLevel -> new Pair<>(event.getDevice(), batteryLevel))
                                )
                        )
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                pair -> ifViewAttached(attachedView -> attachedView.displayReaderDevice(pair.first, pair.second)),
                                Timber::e // TODO handle error
                        )
        );

        // TODO also watch for errors...
    }

    public void disconnect() {
        disposables.add(readerInteractor.disconnect()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> ifViewAttached(ConnectedView::finish)));
    }

    private void disposeSubscriptions() {
        if (disposables != null && !disposables.isDisposed()) {
            disposables.dispose();
        }
    }

    @Override
    public void detachView() {
        disposeSubscriptions();

        super.detachView();
    }

    @Override
    protected void onRequiredConditionsMet() {
        Timber.d("onRequiredConditionsMet");
    }

    @Override
    protected void onMissingPermissionsFound() {
        Timber.e("onMissingPermissionsFound");
    }
}
