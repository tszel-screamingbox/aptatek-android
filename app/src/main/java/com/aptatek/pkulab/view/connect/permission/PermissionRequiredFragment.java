package com.aptatek.pkulab.view.connect.permission;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.view.base.BaseFragment;

import java.util.Collections;
import java.util.List;

import butterknife.OnClick;

public abstract class PermissionRequiredFragment<V extends PermissionRequiredView, P extends PermissionRequiredPresenter<V>> extends BaseFragment<V, P> implements PermissionRequiredView {

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_reader_permission;
    }

    @Override
    protected void initObjects(final View view) {

    }

    @Override
    protected List<View> sensitiveViewList() {
        return Collections.emptyList();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        final FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.getLifecycle().addObserver(this);
        }
    }

    @Override
    public void onDetach() {
        final boolean activity = getActivity() != null;
        if (activity) {
            getActivity().getLifecycle().removeObserver(this);
        }

        super.onDetach();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    @Override
    public void onActivityResumed() {
        if (presenter != null) {
            presenter.checkPermissions();
        }
    }

    @Override
    public void requestPermissions(@NonNull final List<String> missing) {
        // not used on this screen according to figma design
    }


    private void navigateToAppSettings() {
        final Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        final Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    @OnClick(R.id.permissionButton)
    public void onGrantMissingPermissionsClick() {
        navigateToAppSettings();
    }

}
