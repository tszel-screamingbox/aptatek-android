package com.aptatek.pkulab.view.connect.permission;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.View;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.FragmentComponent;
import com.aptatek.pkulab.view.connect.common.BaseConnectScreenFragment;

import javax.inject.Inject;

import butterknife.OnClick;

public class PermissionRequiredFragment extends BaseConnectScreenFragment<PermissionRequiredView, PermissionRequiredPresenter> implements PermissionRequiredView {

    @Inject
    PermissionRequiredPresenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_connect_permission;
    }

    @Override
    protected void initObjects(final View view) {

    }

    @Override
    protected void injectFragment(final FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    public void onActivityResumed() {
        if (presenter.hasAllPermissions()) {
            navigateBack();
        }
    }

    @NonNull
    @Override
    public PermissionRequiredPresenter createPresenter() {
        return presenter;
    }

    @OnClick(R.id.permissionButton)
    public void onGrantMissingPermissionsClick() {
        presenter.grantPermissions();
    }

    @Override
    public void navigateToAppSettings() {
        final Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        final Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

}
