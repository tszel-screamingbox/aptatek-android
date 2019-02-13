package com.aptatek.pkulab.view.connect.permission;

import android.support.v4.content.PermissionChecker;

import com.aptatek.pkulab.domain.interactor.reader.BluetoothInteractor;
import com.aptatek.pkulab.domain.interactor.reader.MissingPermissionsError;
import com.aptatek.pkulab.view.connect.onboarding.ConnectReaderScreen;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.List;

import io.reactivex.disposables.Disposable;
import ix.Ix;
import timber.log.Timber;

public class PermissionRequiredPresenterImpl extends MvpBasePresenter<PermissionRequiredView> implements PermissionRequiredPresenter<PermissionRequiredView> {

    private final BluetoothInteractor bluetoothInteractor;

    private Disposable disposable = null;

    public PermissionRequiredPresenterImpl(final BluetoothInteractor bluetoothInteractor) {
        this.bluetoothInteractor = bluetoothInteractor;
    }

    @Override
    public void checkPermissions() {
        disposeSubscription();

        ifViewAttached(view ->
            disposable = bluetoothInteractor.checkPermissions(((PermissionRequiredFragment) view).getActivity())
                    .subscribe(() -> ifViewAttached(PermissionRequiredView::onConditionsMet),
                            error -> {
                                // ignored here
                            })
        );
    }

    @Override
    public void evaluatePermissionResults(final List<PermissionResult> results) {
        final Boolean hasAllPermissions = Ix.from(results)
                .map(PermissionResult::getResult)
                .map(result -> result == PermissionChecker.PERMISSION_GRANTED)
                .scan((prev, current) -> prev && current)
                .single(false);

        if (hasAllPermissions) {
            ifViewAttached(PermissionRequiredView::onConditionsMet);
        }
    }

    private void disposeSubscription() {
        if (disposable != null) {
            disposable.dispose();
        }
    }

    @Override
    public void detachView() {
        disposeSubscription();

        super.detachView();
    }
}
