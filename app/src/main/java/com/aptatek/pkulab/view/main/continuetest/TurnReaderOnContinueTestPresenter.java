package com.aptatek.pkulab.view.main.continuetest;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.domain.interactor.reader.BluetoothInteractor;
import com.aptatek.pkulab.domain.interactor.reader.ReaderInteractor;
import com.aptatek.pkulab.domain.interactor.test.TestInteractor;
import com.aptatek.pkulab.domain.interactor.testresult.TestResultInteractor;
import com.aptatek.pkulab.domain.model.ContinueTestResultType;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.view.connect.onboarding.turnon.TurnReaderOnConnectView;
import com.aptatek.pkulab.view.connect.permission.PermissionResult;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnPresenter;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnPresenterImpl;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class TurnReaderOnContinueTestPresenter extends MvpBasePresenter<TurnReaderOnContinueTestView>
        implements TurnReaderOnPresenter<TurnReaderOnContinueTestView> {

    private final TurnReaderOnPresenterImpl wrapped;
    private final ReaderInteractor readerInteractor;
    private final TestResultInteractor testResultInteractor;

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public TurnReaderOnContinueTestPresenter(final BluetoothInteractor bluetoothInteractor,
                                             final ReaderInteractor readerInteractor,
                                             final TestResultInteractor testResultInteractor,
                                             final TestInteractor testInteractor) {
        this.testResultInteractor = testResultInteractor;
        wrapped = new TurnReaderOnPresenterImpl(bluetoothInteractor, readerInteractor, testInteractor);
        this.readerInteractor = readerInteractor;
    }

    @Override
    public void attachView(final @NonNull TurnReaderOnContinueTestView view) {
        super.attachView(view);
        wrapped.attachView(view);
    }

    @Override
    public void detachView() {
        wrapped.detachView();

        disposables.dispose();

        super.detachView();
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

    void checkLastMeasure() {
        disposables.add(Single.zip(readerInteractor.getNumberOfResults(), testResultInteractor.getNumberOfMeasures(), Integer::equals)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .flatMap(new Function<Boolean, SingleSource<?>>() {
                    @Override
                    public SingleSource<?> apply(Boolean aBoolean) throws Exception {
                        return readerInteractor.syncResults();
                    }
                })
                .subscribe((isCountEquals, throwable) -> ifViewAttached(view -> {
                    if (isCountEquals) {
                        view.finishTestContinue(ContinueTestResultType.FINISHED_WITH_WRONG_RESULT);
                    } else {
                        view.finishTestContinue(ContinueTestResultType.FINISHED_WITH_CORRECT_RESULT);
                    }
                })));
    }
}
