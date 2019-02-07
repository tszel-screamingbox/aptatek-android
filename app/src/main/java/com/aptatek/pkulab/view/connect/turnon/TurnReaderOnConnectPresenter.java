package com.aptatek.pkulab.view.connect.turnon;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.aptatek.pkulab.domain.interactor.reader.BluetoothInteractor;
import com.aptatek.pkulab.domain.interactor.reader.ReaderInteractor;
import com.aptatek.pkulab.injection.qualifier.ActivityContext;
import com.aptatek.pkulab.view.connect.ConnectReaderScreen;
import com.aptatek.pkulab.view.connect.common.BaseConnectScreenPresenter;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnPresenter;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnPresenterImpl;

import javax.inject.Inject;

public class TurnReaderOnConnectPresenter extends BaseConnectScreenPresenter<TurnReaderOnConnectView> implements TurnReaderOnPresenter<TurnReaderOnConnectView> {

    private final TurnReaderOnPresenterImpl wrapped;

    @Inject
    public TurnReaderOnConnectPresenter(@ActivityContext final Context context,
                                        final BluetoothInteractor bluetoothInteractor,
                                        final ReaderInteractor readerInteractor) {
        super(context);
        wrapped = new TurnReaderOnPresenterImpl(bluetoothInteractor, readerInteractor);
    }

    @Override
    public void attachView(@NonNull final TurnReaderOnConnectView view) {
        wrapped.attachView(view);
        super.attachView(view);
    }

    @Override
    public void detachView() {
        wrapped.detachView();
        super.detachView();
    }

    @Override
    protected void onRequiredConditionsMet() {
//        ifViewAttached(attachedView -> attachedView.showScreen(ConnectReaderScreen.SCAN));
    }

    @Override
    protected void onMissingPermissionsFound() {
        requestMissingPermissions();
    }

    @Override
    public void onResumed() {
        wrapped.onResumed();
    }

    @Override
    public void onPaused() {
        wrapped.onPaused();
    }
}
