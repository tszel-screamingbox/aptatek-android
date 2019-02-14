package com.aptatek.pkulab.view.test.turnreaderon;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.reader.BluetoothInteractor;
import com.aptatek.pkulab.domain.interactor.reader.ReaderInteractor;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.view.connect.permission.PermissionResult;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnPresenter;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnPresenterImpl;
import com.aptatek.pkulab.view.test.base.TestBasePresenter;

import java.util.List;

import javax.inject.Inject;

public class TurnReaderOnTestPresenter extends TestBasePresenter<TurnReaderOnTestView> implements TurnReaderOnPresenter<TurnReaderOnTestView> {

    private final TurnReaderOnPresenterImpl wrapped;

    @Inject
    public TurnReaderOnTestPresenter(final ResourceInteractor resourceInteractor,
                                     final BluetoothInteractor bluetoothInteractor,
                                     final ReaderInteractor readerInteractor) {
        super(resourceInteractor);
        wrapped = new TurnReaderOnPresenterImpl(bluetoothInteractor, readerInteractor);
    }

    @Override
    public void attachView(final @NonNull TurnReaderOnTestView view) {
        super.attachView(view);
        wrapped.attachView(view);
    }

    @Override
    public void detachView() {
        wrapped.detachView();
        super.detachView();
    }

    @Override
    public void initUi() {
        ifViewAttached(attachedView -> attachedView.setNextButtonVisible(false));
    }

    @Override
    public void onResumed() {
        wrapped.onResumed();
    }

    @Override
    public void onPaused() {
        wrapped.onPaused();
    }

    @Override
    public void connectTo(@NonNull final ReaderDevice readerDevice) {
        wrapped.connectTo(readerDevice);
    }

    @Override
    public void evaluatePermissionResults(final List<PermissionResult> results) {
        wrapped.evaluatePermissionResults(results);
    }

    @Override
    public void checkPermissions() {
        wrapped.checkPermissions();
    }
}
