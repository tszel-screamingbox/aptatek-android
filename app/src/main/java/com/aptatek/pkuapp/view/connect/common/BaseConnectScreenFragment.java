package com.aptatek.pkuapp.view.connect.common;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;

import com.aptatek.pkuapp.view.base.BaseFragment;
import com.aptatek.pkuapp.view.connect.ConnectReaderScreen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ix.Ix;

public abstract class BaseConnectScreenFragment<V extends BaseConnectScreenView, P extends BaseConnectScreenPresenter<V>> extends BaseFragment<V, P> implements BaseConnectScreenView {

    private static final int REQ_PERMISSION = 737;

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void showScreen(@NonNull final ConnectReaderScreen screen) {
        if (getActivity() instanceof ConnectCommonView) {
            ((ConnectCommonView) getActivity()).showScreen(screen);
        }
    }

    @Override
    public void onActivityResumed() {

    }

    @Override
    public void navigateBack() {
        if (getActivity() instanceof ConnectCommonView) {
            ((ConnectCommonView) getActivity()).navigateBack();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void requestMissingPermissions(@NonNull List<String> permissions) {
        requestPermissions(permissions.toArray(new String[0]), REQ_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQ_PERMISSION) {
            final List<PermissionResult> permissionResults = Ix.zip(Arrays.asList(permissions), boxedList(grantResults), PermissionResult::create).toList();
            presenter.evaluatePermissionResults(permissionResults);
        }
    }

    private List<Integer> boxedList(@NonNull final int[] primitives) {
        final List<Integer> boxed = new ArrayList<>();

        for (int primitive : primitives) {
            boxed.add(primitive);
        }

        return boxed;
    }
}
