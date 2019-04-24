package com.aptatek.pkulab.view.test.turnreaderon;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.reader.BluetoothInteractor;
import com.aptatek.pkulab.domain.interactor.reader.ReaderInteractor;
import com.aptatek.pkulab.domain.model.reader.ConnectionState;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.view.connect.permission.PermissionResult;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnPresenter;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnPresenterImpl;
import com.aptatek.pkulab.view.test.base.TestBasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class TurnReaderOnTestPresenter extends TestBasePresenter<TurnReaderOnTestView> implements TurnReaderOnPresenter<TurnReaderOnTestView> {

    private final ReaderInteractor readerInteractor;

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public TurnReaderOnTestPresenter(final ResourceInteractor resourceInteractor,
                                     final ReaderInteractor readerInteractor) {
        super(resourceInteractor);
        this.readerInteractor = readerInteractor;
    }

    @Override
    public void detachView() {
        disposables.dispose();

        super.detachView();
    }

    @Override
    public void initUi() {
        ifViewAttached(attachedView -> attachedView.setNextButtonVisible(false));
    }

    @Override
    public void onResumed() {
        getBatteryLevel();
    }

    @Override
    public void onPaused() {
        // do nothing in MOCK
    }

    @Override
    public void connectTo(@NonNull final ReaderDevice readerDevice) {
        // do nothing in MOCK
    }

    @Override
    public void evaluatePermissionResults(final List<PermissionResult> results) {
        // do nothing in MOCK
    }

    @Override
    public void checkPermissions() {
        // do nothing in MOCK
    }

    public void getBatteryLevel() {
        disposables.add(
                readerInteractor.getBatteryLevel()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(batteryPercent -> ifViewAttached(attachedView -> {
                                    attachedView.setBatteryIndicatorVisible(true);
                                    attachedView.setBatteryPercentage(batteryPercent);
                                }),
                                Timber::e
                        )
        );
    }

}
